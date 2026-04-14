<template>
  <div class="app-container">
    <el-card class="mb8" shadow="never">
      <div slot="header"><span>生成节点部署方案</span></div>
      <el-form :inline="true" :model="genForm" size="small">
        <el-form-item label="选择农田">
          <el-select v-model="genForm.farmlandId" placeholder="请选择农田" style="width:180px">
            <el-option v-for="f in farmlandOptions" :key="f.farmlandId" :label="f.farmlandName" :value="f.farmlandId" />
          </el-select>
        </el-form-item>
        <el-form-item label="算法选择">
          <el-radio-group v-model="genForm.algorithmType">
            <el-radio-button label="coverage">
              <i class="el-icon-position"></i> 覆盖优化算法
            </el-radio-button>
            <el-radio-button label="cluster">
              <i class="el-icon-share"></i> 分簇优化算法
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-magic-stick" size="mini" :loading="genLoading" @click="handleGenerate" v-hasPermi="['agricultural:deploy:add']">
            生成部署方案
          </el-button>
        </el-form-item>
      </el-form>
      <div class="algo-desc">
        <el-alert v-if="genForm.algorithmType === 'coverage'" type="info" :closable="false"
          title="覆盖优化算法：采用贪心策略，在网格地图上迭代选择覆盖增益最大的位置放置传感器节点，直到达到目标覆盖率(默认90%)。适合需要最大化监测覆盖面积的场景。" />
        <el-alert v-else type="success" :closable="false"
          title="分簇优化算法：基于LEACH协议思想，结合K-Means聚类将传感器分组，每簇选出簇头节点负责数据汇聚转发，优化网络能耗均衡和通信效率。" />
      </div>
    </el-card>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="所属农田">
        <el-select v-model="queryParams.farmlandId" placeholder="请选择农田" clearable style="width:160px">
          <el-option v-for="f in farmlandOptions" :key="f.farmlandId" :label="f.farmlandName" :value="f.farmlandId" />
        </el-select>
      </el-form-item>
      <el-form-item label="算法类型">
        <el-select v-model="queryParams.algorithmType" placeholder="请选择算法" clearable style="width:130px">
          <el-option label="覆盖优化算法" value="coverage" />
          <el-option label="分簇优化算法" value="cluster" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryParams.status" placeholder="方案状态" clearable style="width:110px">
          <el-option label="待确认" value="0" />
          <el-option label="已确认" value="1" />
          <el-option label="已归档" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['agricultural:deploy:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="schemeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="方案名称" prop="schemeName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="农田" prop="farmlandName" width="120" align="center" />
      <el-table-column label="算法" prop="algorithmType" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.algorithmType === 'coverage' ? 'primary' : 'success'">
            {{ scope.row.algorithmType === 'coverage' ? '覆盖优化' : '分簇优化' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="节点数" prop="nodeCount" width="80" align="center" />
      <el-table-column label="覆盖率(%)" prop="coverageRate" width="100" align="center" />
      <el-table-column label="能耗均衡(%)" prop="energyBalance" width="110" align="center" />
      <el-table-column label="状态" prop="status" width="90" align="center">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
      <el-table-column label="操作" align="center" width="210" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view"
            @click="handleVisualize(scope.row)">可视化</el-button>
          <el-button size="mini" type="text" icon="el-icon-check"
            v-if="scope.row.status === '0'" @click="handleConfirm(scope.row)"
            v-hasPermi="['agricultural:deploy:edit']">确认</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete"
            @click="handleDelete(scope.row)" v-hasPermi="['agricultural:deploy:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listScheme, generateScheme, updateScheme, delScheme } from '@/api/agricultural/deploy'
import { selectFarmlandList } from '@/api/agricultural/farmland'

export default {
  name: 'AgriDeploy',
  data() {
    return {
      loading: false,
      genLoading: false,
      showSearch: true,
      multiple: true,
      total: 0,
      schemeList: [],
      farmlandOptions: [],
      ids: [],
      queryParams: { pageNum: 1, pageSize: 10, farmlandId: null, algorithmType: null, status: null },
      genForm: { farmlandId: null, algorithmType: 'coverage' }
    }
  },
  created() {
    this.getList()
    this.loadFarmlands()
  },
  methods: {
    getList() {
      this.loading = true
      listScheme(this.queryParams).then(res => {
        this.schemeList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    loadFarmlands() {
      selectFarmlandList().then(res => { this.farmlandOptions = res.data || [] })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.schemeId)
      this.multiple = !selection.length
    },
    handleGenerate() {
      if (!this.genForm.farmlandId) { this.$modal.msgError('请先选择农田'); return }
      this.genLoading = true
      generateScheme(this.genForm.farmlandId, this.genForm.algorithmType).then(res => {
        this.$modal.msgSuccess(res.msg || '方案生成成功')
        this.getList()
      }).finally(() => { this.genLoading = false })
    },
    handleVisualize(row) {
      this.$router.push({ path: `/agricultural/deploy/visualize/${row.schemeId}` })
    },
    handleConfirm(row) {
      this.$modal.confirm('是否确认该部署方案？').then(() => {
        updateScheme({ schemeId: row.schemeId, status: '1' }).then(() => {
          this.$modal.msgSuccess('方案已确认')
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const ids = row.schemeId ? [row.schemeId] : this.ids
      this.$modal.confirm('是否确认删除所选部署方案？').then(() => {
        delScheme(ids.join(',')).then(() => { this.getList(); this.$modal.msgSuccess('删除成功') })
      })
    },
    statusType(s) { return s === '1' ? 'success' : s === '2' ? 'info' : 'warning' },
    statusLabel(s) { return s === '1' ? '已确认' : s === '2' ? '已归档' : '待确认' }
  }
}
</script>

<style scoped>
.algo-desc { margin-top: 8px; }
</style>