<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="错误类型" prop="errorType">
        <el-select v-model="queryParams.errorType" placeholder="请选择错误类型" clearable style="width:150px">
          <el-option label="数据格式错误" value="DATA_FORMAT" />
          <el-option label="算法执行失败" value="ALGORITHM_FAIL" />
          <el-option label="文件上传失败" value="UPLOAD_FAIL" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="操作用户" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入用户名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="发生时间">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="-"
          start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="!ids.length" @click="handleDelete" v-hasPermi="['agricultural:errorlog:query']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-delete-solid" size="mini" @click="handleClean" v-hasPermi="['agricultural:errorlog:query']">清空</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList" @selection-change="sel => ids = sel.map(i=>i.errorId)">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="错误ID" prop="errorId" width="80" align="center" />
      <el-table-column label="错误类型" prop="errorType" width="130" align="center">
        <template slot-scope="scope">
          <el-tag :type="typeColor(scope.row.errorType)" size="mini">{{ typeLabel(scope.row.errorType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="错误信息" prop="errorMsg" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="请求地址" prop="requestUrl" width="200" :show-overflow-tooltip="true" />
      <el-table-column label="操作用户" prop="userName" width="100" align="center" />
      <el-table-column label="发生时间" prop="createTime" width="160" align="center" />
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog title="错误详情" :visible.sync="detailOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="错误ID">{{ detail.errorId }}</el-descriptions-item>
        <el-descriptions-item label="错误类型">
          <el-tag :type="typeColor(detail.errorType)" size="mini">{{ typeLabel(detail.errorType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作用户">{{ detail.userName }}</el-descriptions-item>
        <el-descriptions-item label="发生时间">{{ detail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="请求地址" :span="2">{{ detail.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2">{{ detail.errorMsg }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <el-input type="textarea" :rows="3" :value="detail.requestParam" readonly />
        </el-descriptions-item>
      </el-descriptions>
      <el-divider>异常堆栈</el-divider>
      <el-input type="textarea" :rows="10" :value="detail.stackTrace" readonly style="font-family:monospace;font-size:12px" />
      <div slot="footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listErrorLog, delErrorLog, cleanErrorLog, getErrorLog } from '@/api/agricultural/errorlog'

export default {
  name: 'AgriErrorLog',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      logList: [],
      ids: [],
      detailOpen: false,
      detail: {},
      dateRange: [],
      queryParams: { pageNum: 1, pageSize: 10, errorType: null, userName: null, params: {} }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.params = { beginTime: this.dateRange[0] + ' 00:00:00', endTime: this.dateRange[1] + ' 23:59:59' }
      }
      listErrorLog(params).then(res => {
        this.logList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.dateRange = []; this.handleQuery() },
    handleView(row) {
      getErrorLog(row.errorId).then(res => { this.detail = res.data; this.detailOpen = true })
    },
    handleDelete() {
      this.$modal.confirm(`是否确认删除所选的 ${this.ids.length} 条错误日志？`).then(() => {
        delErrorLog(this.ids.join(',')).then(() => { this.$modal.msgSuccess('删除成功'); this.ids = []; this.getList() })
      })
    },
    handleClean() {
      this.$modal.confirm('是否确认清空所有错误日志？此操作不可恢复！').then(() => {
        cleanErrorLog().then(() => { this.$modal.msgSuccess('清空成功'); this.getList() })
      })
    },
    typeColor(t) { return { DATA_FORMAT: 'warning', ALGORITHM_FAIL: 'danger', UPLOAD_FAIL: 'info', OTHER: '' }[t] || '' },
    typeLabel(t) { return { DATA_FORMAT: '数据格式错误', ALGORITHM_FAIL: '算法执行失败', UPLOAD_FAIL: '上传失败', OTHER: '其他' }[t] || t }
  }
}
</script>