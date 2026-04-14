<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="农田名称" prop="farmlandName">
        <el-input v-model="queryParams.farmlandName" placeholder="请输入农田名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="种植作物" prop="cropType">
        <el-input v-model="queryParams.cropType" placeholder="请输入种植作物类型" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="农田状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['agricultural:farmland:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['agricultural:farmland:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['agricultural:farmland:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="farmlandList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="农田ID" prop="farmlandId" width="80" align="center" />
      <el-table-column label="农田名称" prop="farmlandName" :show-overflow-tooltip="true" />
      <el-table-column label="种植作物" prop="cropType" width="120" align="center" />
      <el-table-column label="面积(亩)" prop="area" width="100" align="center" />
      <el-table-column label="网格(行x列)" align="center" width="120">
        <template slot-scope="scope">{{ scope.row.gridRows }} x {{ scope.row.gridCols }}</template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">{{ scope.row.status === '0' ? '正常' : '停用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
      <el-table-column label="操作" align="center" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['agricultural:farmland:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['agricultural:farmland:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="农田名称" prop="farmlandName">
          <el-input v-model="form.farmlandName" placeholder="请输入农田名称" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="面积(亩)" prop="area">
              <el-input-number v-model="form.area" :precision="2" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="种植作物" prop="cropType">
              <el-input v-model="form.cropType" placeholder="如：水稻、小麦等" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="网格行数" prop="gridRows">
              <el-input-number v-model="form.gridRows" :min="5" :max="100" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网格列数" prop="gridCols">
              <el-input-number v-model="form.gridCols" :min="5" :max="100" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="边界数据" prop="boundaryData">
          <el-input v-model="form.boundaryData" type="textarea" :rows="3" placeholder='如：[[0,0],[100,0],[100,80],[0,80]]' />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFarmland, getFarmland, addFarmland, updateFarmland, delFarmland } from '@/api/agricultural/farmland'

export default {
  name: 'AgriFarmland',
  data() {
    return {
      loading: false,
      showSearch: true,
      single: true,
      multiple: true,
      total: 0,
      farmlandList: [],
      title: '',
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, farmlandName: null, cropType: null, status: null },
      form: {},
      rules: {
        farmlandName: [{ required: true, message: '农田名称不能为空', trigger: 'blur' }],
        gridRows: [{ required: true, message: '网格行数不能为空', trigger: 'blur' }],
        gridCols: [{ required: true, message: '网格列数不能为空', trigger: 'blur' }]
      },
      ids: []
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listFarmland(this.queryParams).then(res => {
        this.farmlandList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    reset() {
      this.form = { farmlandName: '', boundaryData: '', area: null, cropType: '', gridRows: 20, gridCols: 20, status: '0', remark: '' }
      this.$refs.form && this.$refs.form.resetFields()
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.farmlandId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() { this.reset(); this.open = true; this.title = '新增农田' },
    handleUpdate(row) {
      this.reset()
      const id = row.farmlandId || this.ids[0]
      getFarmland(id).then(res => { this.form = res.data; this.open = true; this.title = '修改农田' })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.farmlandId) {
          updateFarmland(this.form).then(() => { this.$modal.msgSuccess('修改成功'); this.open = false; this.getList() })
        } else {
          addFarmland(this.form).then(() => { this.$modal.msgSuccess('新增成功'); this.open = false; this.getList() })
        }
      })
    },
    cancel() { this.open = false; this.reset() },
    handleDelete(row) {
      const ids = row.farmlandId || this.ids
      this.$modal.confirm('是否确认删除所选农田数据？').then(() => {
        delFarmland(ids).then(() => { this.getList(); this.$modal.msgSuccess('删除成功') })
      })
    }
  }
}
</script>