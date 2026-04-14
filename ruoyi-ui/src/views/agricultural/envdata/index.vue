<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="所属农田" prop="farmlandId">
        <el-select v-model="queryParams.farmlandId" placeholder="请选择农田" clearable style="width:160px">
          <el-option v-for="f in farmlandOptions" :key="f.farmlandId" :label="f.farmlandName" :value="f.farmlandId" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['agricultural:envdata:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-cpu" size="mini" @click="handleGenerate" v-hasPermi="['agricultural:envdata:generate']">模拟生成数据</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['agricultural:envdata:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="农田名称" prop="farmlandName" :show-overflow-tooltip="true" />
      <el-table-column label="温度(°C)" prop="temperature" width="100" align="center" />
      <el-table-column label="湿度(%)" prop="humidity" width="100" align="center" />
      <el-table-column label="光照(lux)" prop="lightIntensity" width="120" align="center" />
      <el-table-column label="土壤含水量(%)" prop="soilMoisture" width="120" align="center" />
      <el-table-column label="风速(m/s)" prop="windSpeed" width="100" align="center" />
      <el-table-column label="记录时间" prop="recordTime" width="160" align="center" />
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['agricultural:envdata:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog title="新增环境数据" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属农田" prop="farmlandId">
          <el-select v-model="form.farmlandId" placeholder="请选择农田" style="width:100%">
            <el-option v-for="f in farmlandOptions" :key="f.farmlandId" :label="f.farmlandName" :value="f.farmlandId" />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="温度(°C)" prop="temperature">
              <el-input-number v-model="form.temperature" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="湿度(%)" prop="humidity">
              <el-input-number v-model="form.humidity" :precision="2" :min="0" :max="100" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="光照(lux)">
              <el-input-number v-model="form.lightIntensity" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="土壤含水量(%)">
              <el-input-number v-model="form.soilMoisture" :precision="2" :min="0" :max="100" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="风速(m/s)">
              <el-input-number v-model="form.windSpeed" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="记录时间" prop="recordTime">
              <el-date-picker v-model="form.recordTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="批量模拟生成环境数据" :visible.sync="generateOpen" width="400px" append-to-body>
      <el-form ref="genForm" :model="genForm" label-width="100px">
        <el-form-item label="所属农田" prop="farmlandId">
          <el-select v-model="genForm.farmlandId" placeholder="请选择农田" style="width:100%">
            <el-option v-for="f in farmlandOptions" :key="f.farmlandId" :label="f.farmlandName" :value="f.farmlandId" />
          </el-select>
        </el-form-item>
        <el-form-item label="生成数量" prop="count">
          <el-input-number v-model="genForm.count" :min="1" :max="500" style="width:100%" />
          <div class="el-form-item__error" style="position:static;color:#909399;font-size:12px">最多500条记录，每条记录时间随机分布在近7天内</div>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" :loading="genLoading" @click="submitGenerate">生 成</el-button>
        <el-button @click="generateOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEnvData, addEnvData, delEnvData, generateEnvData } from '@/api/agricultural/envdata'
import { selectFarmlandList } from '@/api/agricultural/farmland'

export default {
  name: 'AgriEnvData',
  data() {
    return {
      loading: false,
      showSearch: true,
      multiple: true,
      total: 0,
      dataList: [],
      open: false,
      generateOpen: false,
      genLoading: false,
      farmlandOptions: [],
      ids: [],
      queryParams: { pageNum: 1, pageSize: 10, farmlandId: null },
      form: {},
      genForm: { farmlandId: null, count: 50 },
      rules: {
        farmlandId: [{ required: true, message: '请选择农田', trigger: 'change' }],
        recordTime: [{ required: true, message: '请选择记录时间', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
    this.loadFarmlands()
  },
  methods: {
    getList() {
      this.loading = true
      listEnvData(this.queryParams).then(res => {
        this.dataList = res.rows
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
      this.ids = selection.map(item => item.dataId)
      this.multiple = !selection.length
    },
    handleAdd() {
      this.form = { farmlandId: null, temperature: null, humidity: null, lightIntensity: null, soilMoisture: null, windSpeed: null, recordTime: null }
      this.open = true
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        addEnvData(this.form).then(() => {
          this.$modal.msgSuccess('新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleGenerate() {
      this.genForm = { farmlandId: null, count: 50 }
      this.generateOpen = true
    },
    submitGenerate() {
      if (!this.genForm.farmlandId) { this.$modal.msgError('请选择农田'); return }
      this.genLoading = true
      generateEnvData(this.genForm.farmlandId, this.genForm.count).then(res => {
        this.$modal.msgSuccess(res.msg)
        this.generateOpen = false
        this.getList()
      }).finally(() => { this.genLoading = false })
    },
    handleDelete(row) {
      const ids = row.dataId ? [row.dataId] : this.ids
      this.$modal.confirm('是否确认删除所选环境数据？').then(() => {
        delEnvData(ids.join(',')).then(() => { this.getList(); this.$modal.msgSuccess('删除成功') })
      })
    }
  }
}
</script>