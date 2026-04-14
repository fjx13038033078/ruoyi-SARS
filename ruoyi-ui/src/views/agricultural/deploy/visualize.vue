<template>
  <div class="app-container visualize-container">
    <el-card class="info-card mb8" shadow="never">
      <el-row :gutter="20" align="middle" type="flex">
        <el-col :span="14">
          <span class="scheme-title">{{ schemeInfo.schemeName }}</span>
          <el-tag size="small" class="ml8" :type="schemeInfo.algorithmType === 'coverage' ? 'primary' : 'success'">
            {{ schemeInfo.algorithmType === 'coverage' ? '覆盖优化算法' : '分簇优化算法' }}
          </el-tag>
        </el-col>
        <el-col :span="10" class="stat-row">
          <span class="stat-item"><i class="el-icon-s-grid"></i> 农田：{{ schemeInfo.farmlandName }}</span>
          <span class="stat-item"><i class="el-icon-location"></i> 节点数：{{ schemeInfo.nodeCount }}</span>
          <span class="stat-item"><i class="el-icon-pie-chart"></i> 覆盖率：{{ schemeInfo.coverageRate }}%</span>
          <span class="stat-item"><i class="el-icon-lightning"></i> 能耗均衡：{{ schemeInfo.energyBalance }}%</span>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="12">
      <el-col :span="5">
        <el-card shadow="never" class="legend-card">
          <div slot="header">图例</div>
          <div class="legend-item"><span class="legend-dot sensor"></span>传感器节点</div>
          <div class="legend-item"><span class="legend-dot sink"></span>簇头/汇聚节点</div>
          <div class="legend-item"><span class="legend-circle"></span>覆盖范围</div>
          <div class="legend-item"><span class="legend-grid"></span>网格单元</div>

          <el-divider>操作提示</el-divider>
          <div class="tip-text">? 滚轮缩放地图</div>
          <div class="tip-text">? 拖拽平移视图</div>
          <div class="tip-text">? 悬停节点查看详情</div>
          <div class="tip-text">? 点击节点高亮同簇</div>

          <el-divider>快捷操作</el-divider>
          <el-button size="mini" icon="el-icon-refresh-right" @click="resetView" style="width:100%">重置视图</el-button>
          <el-button size="mini" icon="el-icon-view" @click="toggleCoverage" style="width:100%;margin-top:6px">
            {{ showCoverage ? '隐藏覆盖范围' : '显示覆盖范围' }}
          </el-button>
          <el-button size="mini" icon="el-icon-s-grid" @click="toggleGrid" style="width:100%;margin-top:6px">
            {{ showGrid ? '隐藏网格线' : '显示网格线' }}
          </el-button>

          <el-divider>节点列表</el-divider>
          <div class="node-list">
            <div v-for="node in nodes" :key="node.nodeId"
              class="node-item" :class="{ active: hoveredNode && hoveredNode.nodeId === node.nodeId }"
              @click="focusNode(node)">
              <span class="node-dot" :class="node.nodeType"></span>
              <span class="node-code">{{ node.nodeCode }}</span>
              <span class="node-pos">({{ node.posX }}, {{ node.posY }})</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="19">
        <el-card shadow="never" class="canvas-card">
          <div slot="header">
            <span>节点部署可视化地图</span>
            <span style="float:right;color:#909399;font-size:12px">网格: {{ gridRows }} x {{ gridCols }}</span>
          </div>
          <div class="canvas-wrapper" ref="canvasWrapper">
            <canvas ref="mapCanvas"
              @mousedown="onMouseDown"
              @mousemove="onMouseMove"
              @mouseup="onMouseUp"
              @mouseleave="onMouseLeave"
              @wheel.prevent="onWheel">
            </canvas>
            <div v-if="tooltip.visible" class="canvas-tooltip" :style="{ left: tooltip.x + 'px', top: tooltip.y + 'px' }">
              <div class="tt-title">{{ tooltip.nodeCode }}</div>
              <div>类型：{{ tooltip.nodeType === 'sink' ? '簇头/汇聚节点' : '传感器节点' }}</div>
              <div>坐标：({{ tooltip.posX }}, {{ tooltip.posY }})</div>
              <div>覆盖半径：{{ tooltip.coverageRadius }}</div>
              <div>能量水平：{{ tooltip.energyLevel }}%</div>
              <div v-if="tooltip.clusterId > 0">簇 ID：{{ tooltip.clusterId }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getScheme, getSchemeNodes } from '@/api/agricultural/deploy'

export default {
  name: 'DeployVisualize',
  data() {
    return {
      schemeId: null,
      schemeInfo: {},
      nodes: [],
      gridRows: 20,
      gridCols: 20,
      showCoverage: true,
      showGrid: true,
      hoveredNode: null,
      canvas: null,
      ctx: null,
      scale: 1,
      offsetX: 0,
      offsetY: 0,
      isDragging: false,
      dragStart: { x: 0, y: 0 },
      cellSize: 24,
      highlightCluster: null,
      tooltip: { visible: false, x: 0, y: 0, nodeCode: '', nodeType: '', posX: 0, posY: 0, coverageRadius: 0, energyLevel: 0, clusterId: 0 }
    }
  },
  mounted() {
    this.schemeId = this.$route.params.schemeId
    this.loadData()
  },
  methods: {
    async loadData() {
      const [s, n] = await Promise.all([getScheme(this.schemeId), getSchemeNodes(this.schemeId)])
      this.schemeInfo = s.data
      this.nodes = n.data || []
      this.gridRows = 20
      this.gridCols = 20
      this.$nextTick(() => this.initCanvas())
    },
    initCanvas() {
      const wrapper = this.$refs.canvasWrapper
      this.canvas = this.$refs.mapCanvas
      this.canvas.width = wrapper.clientWidth
      this.canvas.height = Math.max(480, wrapper.clientWidth * 0.6)
      this.ctx = this.canvas.getContext('2d')
      this.resetView(false)
      this.draw()
    },
    resetView(redraw = true) {
      const padding = 40
      const maxCols = this.gridCols || 20
      const maxRows = this.gridRows || 20
      const availW = this.canvas.width - padding * 2
      const availH = this.canvas.height - padding * 2
      this.cellSize = Math.min(availW / maxCols, availH / maxRows)
      this.scale = 1
      this.offsetX = padding
      this.offsetY = padding
      if (redraw) this.draw()
    },
    draw() {
      const { ctx, canvas, scale, offsetX, offsetY, cellSize, gridRows, gridCols } = this
      ctx.clearRect(0, 0, canvas.width, canvas.height)
      const cs = cellSize * scale

      ctx.fillStyle = '#f0f9eb'
      ctx.fillRect(offsetX, offsetY, cs * gridCols, cs * gridRows)

      if (this.showGrid) {
        ctx.strokeStyle = '#d4e8c2'
        ctx.lineWidth = 0.5
        for (let r = 0; r <= gridRows; r++) {
          ctx.beginPath()
          ctx.moveTo(offsetX, offsetY + r * cs)
          ctx.lineTo(offsetX + gridCols * cs, offsetY + r * cs)
          ctx.stroke()
        }
        for (let c = 0; c <= gridCols; c++) {
          ctx.beginPath()
          ctx.moveTo(offsetX + c * cs, offsetY)
          ctx.lineTo(offsetX + c * cs, offsetY + gridRows * cs)
          ctx.stroke()
        }
      }

      ctx.strokeStyle = '#5b8c3d'
      ctx.lineWidth = 2
      ctx.strokeRect(offsetX, offsetY, cs * gridCols, cs * gridRows)

      if (this.showCoverage) {
        this.nodes.forEach(node => {
          const cx = offsetX + Number(node.posX) * cs
          const cy = offsetY + Number(node.posY) * cs
          const r = Number(node.coverageRadius) * cs
          const isHighlight = this.highlightCluster !== null && node.clusterId === this.highlightCluster
          ctx.beginPath()
          ctx.arc(cx, cy, r, 0, Math.PI * 2)
          ctx.fillStyle = node.nodeType === 'sink'
            ? (isHighlight ? 'rgba(255,100,100,0.18)' : 'rgba(255,100,100,0.08)')
            : (isHighlight ? 'rgba(64,158,255,0.22)' : 'rgba(64,158,255,0.10)')
          ctx.fill()
          ctx.strokeStyle = node.nodeType === 'sink' ? 'rgba(255,80,80,0.5)' : 'rgba(64,158,255,0.4)'
          ctx.lineWidth = 1
          ctx.setLineDash([4, 3])
          ctx.stroke()
          ctx.setLineDash([])
        })
      }

      this.nodes.forEach(node => {
        const cx = offsetX + Number(node.posX) * cs
        const cy = offsetY + Number(node.posY) * cs
        const isHovered = this.hoveredNode && this.hoveredNode.nodeId === node.nodeId
        const r = isHovered ? (node.nodeType === 'sink' ? 9 : 7) : (node.nodeType === 'sink' ? 7 : 5)

        ctx.beginPath()
        ctx.arc(cx, cy, r, 0, Math.PI * 2)
        ctx.fillStyle = node.nodeType === 'sink' ? '#e74c3c' : '#409eff'
        ctx.fill()
        ctx.strokeStyle = '#fff'
        ctx.lineWidth = 1.5
        ctx.stroke()

        if (cs > 12 || isHovered) {
          ctx.fillStyle = '#333'
          ctx.font = `${isHovered ? 'bold ' : ''}${Math.max(10, cs * 0.4)}px sans-serif`
          ctx.textAlign = 'center'
          ctx.fillText(node.nodeCode, cx, cy - r - 3)
        }
      })
    },
    toGridCoord(canvasX, canvasY) {
      const { offsetX, offsetY, cellSize, scale } = this
      const cs = cellSize * scale
      return { x: (canvasX - offsetX) / cs, y: (canvasY - offsetY) / cs }
    },
    findNodeAt(canvasX, canvasY) {
      const cs = this.cellSize * this.scale
      for (const node of this.nodes) {
        const cx = this.offsetX + Number(node.posX) * cs
        const cy = this.offsetY + Number(node.posY) * cs
        const r = node.nodeType === 'sink' ? 9 : 7
        if (Math.hypot(canvasX - cx, canvasY - cy) <= r + 4) return node
      }
      return null
    },
    onMouseDown(e) {
      const rect = this.canvas.getBoundingClientRect()
      const mx = e.clientX - rect.left
      const my = e.clientY - rect.top
      const node = this.findNodeAt(mx, my)
      if (node) {
        this.highlightCluster = (this.highlightCluster === node.clusterId) ? null : node.clusterId
        this.draw()
      } else {
        this.isDragging = true
        this.dragStart = { x: e.clientX - this.offsetX, y: e.clientY - this.offsetY }
      }
    },
    onMouseMove(e) {
      const rect = this.canvas.getBoundingClientRect()
      const mx = e.clientX - rect.left
      const my = e.clientY - rect.top
      if (this.isDragging) {
        this.offsetX = e.clientX - this.dragStart.x
        this.offsetY = e.clientY - this.dragStart.y
        this.draw()
        return
      }
      const node = this.findNodeAt(mx, my)
      if (node) {
        this.canvas.style.cursor = 'pointer'
        this.hoveredNode = node
        this.tooltip = {
          visible: true,
          x: mx + 15,
          y: my - 10,
          nodeCode: node.nodeCode,
          nodeType: node.nodeType,
          posX: node.posX,
          posY: node.posY,
          coverageRadius: node.coverageRadius,
          energyLevel: node.energyLevel,
          clusterId: node.clusterId || 0
        }
      } else {
        this.canvas.style.cursor = 'grab'
        this.hoveredNode = null
        this.tooltip.visible = false
      }
      this.draw()
    },
    onMouseUp() { this.isDragging = false },
    onMouseLeave() { this.isDragging = false; this.tooltip.visible = false; this.hoveredNode = null },
    onWheel(e) {
      const rect = this.canvas.getBoundingClientRect()
      const mx = e.clientX - rect.left
      const my = e.clientY - rect.top
      const delta = e.deltaY > 0 ? 0.9 : 1.1
      const newScale = Math.max(0.2, Math.min(10, this.scale * delta))
      this.offsetX = mx - (mx - this.offsetX) * (newScale / this.scale)
      this.offsetY = my - (my - this.offsetY) * (newScale / this.scale)
      this.scale = newScale
      this.draw()
    },
    toggleCoverage() { this.showCoverage = !this.showCoverage; this.draw() },
    toggleGrid() { this.showGrid = !this.showGrid; this.draw() },
    focusNode(node) {
      const cs = this.cellSize * this.scale
      this.offsetX = this.canvas.width / 2 - Number(node.posX) * cs
      this.offsetY = this.canvas.height / 2 - Number(node.posY) * cs
      this.hoveredNode = node
      this.draw()
    }
  }
}
</script>

<style scoped>
.visualize-container { padding-bottom: 20px; }
.info-card .scheme-title { font-size: 16px; font-weight: bold; color: #303133; }
.ml8 { margin-left: 8px; }
.stat-row { display: flex; align-items: center; gap: 16px; flex-wrap: wrap; }
.stat-item { font-size: 13px; color: #606266; white-space: nowrap; }
.stat-item i { margin-right: 4px; }
.legend-card { height: 100%; }
.legend-item { display: flex; align-items: center; gap: 8px; margin: 6px 0; font-size: 13px; color: #606266; }
.legend-dot { width: 12px; height: 12px; border-radius: 50%; display: inline-block; }
.legend-dot.sensor { background: #409eff; }
.legend-dot.sink { background: #e74c3c; }
.legend-circle { width: 18px; height: 18px; border-radius: 50%; border: 1.5px dashed #409eff; background: rgba(64,158,255,0.1); display: inline-block; }
.legend-grid { width: 16px; height: 16px; border: 1px solid #d4e8c2; background: #f0f9eb; display: inline-block; }
.tip-text { font-size: 12px; color: #909399; margin: 3px 0; }
.node-list { max-height: 240px; overflow-y: auto; }
.node-item { display: flex; align-items: center; gap: 6px; padding: 4px 6px; border-radius: 4px; cursor: pointer; font-size: 12px; color: #606266; }
.node-item:hover, .node-item.active { background: #ecf5ff; }
.node-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.node-dot.sensor { background: #409eff; }
.node-dot.sink { background: #e74c3c; }
.node-code { font-weight: bold; min-width: 60px; }
.node-pos { color: #909399; }
.canvas-card { }
.canvas-wrapper { position: relative; background: #fafafa; border-radius: 4px; overflow: hidden; }
.canvas-wrapper canvas { display: block; cursor: grab; }
.canvas-tooltip {
  position: absolute;
  background: rgba(0,0,0,0.75);
  color: #fff;
  padding: 8px 12px;
  border-radius: 6px;
  font-size: 12px;
  pointer-events: none;
  z-index: 10;
  line-height: 1.8;
  min-width: 140px;
}
.tt-title { font-weight: bold; font-size: 13px; margin-bottom: 2px; }
</style>