<template>
  <div class="app-container">
    <el-card class="mb8 post-card" shadow="never">
      <div slot="header"><span>发表评论</span></div>
      <el-form ref="postForm" :model="postForm" :rules="postRules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="评论对象" prop="targetType">
              <el-select v-model="postForm.targetType" placeholder="请选择" style="width:100%">
                <el-option label="系统整体评价" value="system" />
                <el-option label="算法效果反馈" value="algorithm" />
                <el-option label="可视化展示体验" value="visualize" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评分">
              <el-rate v-model="postForm.rating" show-score />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="评论内容" prop="content">
          <el-input v-model="postForm.content" type="textarea" :rows="4" placeholder="请输入您的意见或建议，帮助我们改进系统功能..." />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="昵称">
              <el-input v-model="postForm.userName" placeholder="留空则使用登录用户名" :disabled="postForm.isAnonymous === 'Y'" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="匿名评论">
              <el-switch v-model="postForm.isAnonymous" active-value="Y" inactive-value="N" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" icon="el-icon-s-promotion" :loading="posting" @click="submitComment" v-hasPermi="['agricultural:comment:add']">发表评论</el-button>
          <el-button @click="resetPost">重 置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <div slot="header">
        <span>评论列表</span>
        <el-radio-group v-model="queryParams.targetType" size="mini" style="margin-left:16px" @change="handleQuery">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="system">系统整体评价</el-radio-button>
          <el-radio-button label="algorithm">算法效果反馈</el-radio-button>
          <el-radio-button label="visualize">可视化展示体验</el-radio-button>
        </el-radio-group>
        <el-button size="mini" type="danger" plain icon="el-icon-delete" style="float:right" :disabled="!ids.length"
          @click="handleBatchDelete" v-hasPermi="['agricultural:comment:remove']">批量删除</el-button>
      </div>

      <el-table v-loading="loading" :data="commentList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="评论者" width="110" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.isAnonymous === 'Y'" style="color:#909399">
              <i class="el-icon-user"></i> 匿名用户
            </span>
            <span v-else>{{ scope.row.userName || scope.row.createBy }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评论对象" width="110" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="info">{{ targetLabel(scope.row.targetType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评分" width="130" align="center">
          <template slot-scope="scope">
            <el-rate :value="scope.row.rating" disabled show-score />
          </template>
        </el-table-column>
        <el-table-column label="评论内容" prop="content" :show-overflow-tooltip="true" min-width="260" />
        <el-table-column label="图片" width="80" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.imageUrls">
              <el-image v-for="(url, i) in scope.row.imageUrls.split(',')" :key="i"
                :src="url" style="width:30px;height:30px;margin:1px" fit="cover"
                :preview-src-list="scope.row.imageUrls.split(',')" />
            </span>
            <span v-else style="color:#c0c4cc">无</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" size="mini">
              {{ scope.row.status === '0' ? '正常' : '已屏蔽' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评论时间" prop="createTime" width="160" align="center" />
        <el-table-column label="操作" width="130" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === '0'" size="mini" type="text" style="color:#E6A23C"
              @click="handleHide(scope.row)" v-hasPermi="['agricultural:comment:edit']">屏蔽</el-button>
            <el-button v-else size="mini" type="text" style="color:#67C23A"
              @click="handleShow(scope.row)" v-hasPermi="['agricultural:comment:edit']">显示</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C"
              @click="handleDelete(scope.row)" v-hasPermi="['agricultural:comment:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </el-card>
  </div>
</template>

<script>
import { listComment, addComment, delComment, updateCommentStatus } from '@/api/agricultural/comment'

export default {
  name: 'AgriComment',
  data() {
    return {
      loading: false,
      posting: false,
      total: 0,
      commentList: [],
      ids: [],
      queryParams: { pageNum: 1, pageSize: 10, targetType: '', status: null },
      postForm: { targetType: 'system', content: '', rating: 5, isAnonymous: 'N', userName: '' },
      postRules: {
        targetType: [{ required: true, message: '请选择评论对象类型', trigger: 'change' }],
        content: [{ required: true, message: '评论内容不能为空', trigger: 'blur' }, { min: 5, message: '至少输入5个字符', trigger: 'blur' }]
      }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (!params.targetType) delete params.targetType
      listComment(params).then(res => {
        this.commentList = res.rows
        this.total = res.total
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    handleSelectionChange(sel) { this.ids = sel.map(i => i.commentId) },
    submitComment() {
      this.$refs.postForm.validate(valid => {
        if (!valid) return
        this.posting = true
        addComment(this.postForm).then(() => {
          this.$modal.msgSuccess('评论发表成功，感谢您的反馈！')
          this.resetPost()
          this.getList()
        }).finally(() => { this.posting = false })
      })
    },
    resetPost() {
      this.postForm = { targetType: 'system', content: '', rating: 5, isAnonymous: 'N', userName: '' }
      this.$refs.postForm && this.$refs.postForm.resetFields()
    },
    handleHide(row) { updateCommentStatus(row.commentId, '1').then(() => { this.$modal.msgSuccess('已屏蔽'); this.getList() }) },
    handleShow(row) { updateCommentStatus(row.commentId, '0').then(() => { this.$modal.msgSuccess('已恢复显示'); this.getList() }) },
    handleDelete(row) {
      this.$modal.confirm('是否确认删除该评论？').then(() => {
        delComment(row.commentId).then(() => { this.$modal.msgSuccess('删除成功'); this.getList() })
      })
    },
    handleBatchDelete() {
      this.$modal.confirm(`是否确认删除所选的 ${this.ids.length} 条评论？`).then(() => {
        delComment(this.ids.join(',')).then(() => { this.$modal.msgSuccess('删除成功'); this.ids = []; this.getList() })
      })
    },
    targetLabel(t) {
      const map = { system: '系统整体评价', algorithm: '算法效果反馈', visualize: '可视化展示体验', scheme: '部署方案' }
      return map[t] || t || '其他'
    }
  }
}
</script>

<style scoped>
.post-card { border-left: 4px solid #409eff; }
</style>