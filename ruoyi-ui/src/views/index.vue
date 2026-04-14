<template>
  <div class="home-dashboard">
    <!-- 顶部标题区 -->
    <section class="home-hero">
      <div class="home-hero__inner">
        <h1 class="home-hero__title">智慧农联节点部署推荐系统</h1>
        <p class="home-hero__subtitle">
          融合农田档案、栅格化覆盖与聚类部署算法，辅助传感器布点决策与环境监测分析。
        </p>
      </div>
    </section>

    <el-row :gutter="24" class="home-main-row">
      <!-- 核心功能介绍与导航 -->
      <el-col :xs="24" :lg="15" :xl="16">
        <el-card class="home-card home-card--features" shadow="hover">
          <div slot="header" class="home-card__header">
            <i class="el-icon-s-grid home-card__header-icon" />
            <span>核心功能</span>
          </div>
          <p class="home-feature-intro">
            系统以农田多边形与栅格为输入，支持<strong>覆盖优化</strong>（贪心圆盘覆盖）与<strong>聚类优化</strong>（K-Means 分簇选簇头）两类部署策略；
            并可管理环境监测时序数据、查看部署可视化与运维反馈。下方入口将跳转至对应业务页面（需具备相应菜单权限）。
          </p>
          <div class="feature-grid">
            <div
              v-for="item in featureEntries"
              :key="item.path"
              class="feature-tile"
              role="button"
              tabindex="0"
              @click="goFeature(item)"
              @keyup.enter="goFeature(item)"
            >
              <div class="feature-tile__icon-wrap" :class="'feature-tile__icon-wrap--' + item.tone">
                <i :class="item.icon" />
              </div>
              <div class="feature-tile__body">
                <div class="feature-tile__title">{{ item.title }}</div>
                <div class="feature-tile__desc">{{ item.desc }}</div>
                <span class="feature-tile__action">进入 <i class="el-icon-right" /></span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 通知公告 -->
      <el-col :xs="24" :lg="9" :xl="8">
        <el-card class="home-card home-card--notice" shadow="hover">
          <div slot="header" class="home-card__header">
            <i class="el-icon-bell home-card__header-icon" />
            <span>通知公告</span>
          </div>
          <div v-loading="loading" class="notice-body">
            <el-table
              v-if="noticeList.length"
              :data="noticeList"
              size="small"
              stripe
              class="notice-table"
              :show-header="true"
              max-height="420"
            >
              <el-table-column label="标题" min-width="120" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <el-link type="primary" :underline="false" @click="showNoticeContent(scope.row)">
                    {{ scope.row.noticeTitle }}
                  </el-link>
                </template>
              </el-table-column>
              <el-table-column label="类型" align="center" width="72">
                <template slot-scope="scope">
                  <dict-tag :options="dict.type.sys_notice_type" :value="scope.row.noticeType" />
                </template>
              </el-table-column>
              <el-table-column label="日期" align="center" width="88">
                <template slot-scope="scope">
                  <span class="notice-date">{{ parseTime(scope.row.createTime, '{m}-{d}') }}</span>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-else description="暂无公告" :image-size="72" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      :title="selectedNotice.title"
      :visible.sync="showNoticeDialog"
      width="780px"
      append-to-body
      custom-class="home-notice-dialog"
    >
      <div slot="title" class="notice-dialog-title">{{ selectedNotice.title }}</div>
      <div v-html="selectedNotice.content" class="notice-content" />
    </el-dialog>
  </div>
</template>

<script>
import { parseTime } from '@/utils/ruoyi'
import { getNotice, listNotice } from '@/api/system/notice'

export default {
  name: 'Index',
  dicts: ['sys_notice_type'],
  data() {
    return {
      loading: true,
      noticeList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      selectedNotice: {
        title: '',
        content: ''
      },
      showNoticeDialog: false,
      featureEntries: [
        {
          path: '/agricultural/farmland',
          title: '农田管理',
          desc: '维护农田边界、面积、作物与栅格规模，为部署算法提供空间基础。',
          icon: 'el-icon-map-location',
          tone: 'green',
          perm: 'agricultural:farmland:list'
        },
        {
          path: '/agricultural/deploy',
          title: '节点部署',
          desc: '生成覆盖/聚类部署方案，查看节点列表与可视化结果。',
          icon: 'el-icon-position',
          tone: 'teal',
          perm: 'agricultural:deploy:list'
        },
        {
          path: '/agricultural/envdata',
          title: '环境监测',
          desc: '查询与录入温湿度、光照、土壤湿度等时序环境数据。',
          icon: 'el-icon-data-line',
          tone: 'blue',
          perm: 'agricultural:envdata:list'
        },
        {
          path: '/agricultural/comment',
          title: '互动评论',
          desc: '针对农田或方案等对象发表评价与反馈。',
          icon: 'el-icon-chat-dot-round',
          tone: 'amber',
          perm: 'agricultural:comment:list'
        },
        {
          path: '/agricultural/errorlog',
          title: '异常日志',
          desc: '查看农业模块异步记录的异常与请求上下文，便于运维排查。',
          icon: 'el-icon-warning-outline',
          tone: 'rose',
          perm: 'agricultural:errorlog:list'
        }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parseTime,
    getList() {
      this.loading = true
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    showNoticeContent(row) {
      this.loading = true
      getNotice(row.noticeId).then(response => {
        this.selectedNotice.title = response.data.noticeTitle
        this.selectedNotice.content = response.data.noticeContent
        this.showNoticeDialog = true
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    goFeature(item) {
      if (item.perm && !this.$auth.hasPermi(item.perm)) {
        this.$modal.msgWarning('您暂无访问该功能的权限，请联系管理员分配菜单权限。')
        return
      }
      this.$router.push(item.path)
    }
  }
}
</script>

<style scoped lang="scss">
.home-dashboard {
  padding: 16px 20px 28px;
}

.home-hero {
  margin: 0 0 24px;
  padding: 32px 28px 36px;
  background: linear-gradient(135deg, #1a5f4a 0%, #2d8a6e 42%, #3daf8c 100%);
  color: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(26, 95, 74, 0.22);
}

.home-hero__inner {
  max-width: 920px;
}

.home-hero__title {
  margin: 0 0 12px;
  font-size: 26px;
  font-weight: 600;
  letter-spacing: 0.04em;
  line-height: 1.35;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.12);
}

.home-hero__subtitle {
  margin: 0;
  font-size: 15px;
  line-height: 1.65;
  opacity: 0.92;
  max-width: 640px;
}

.home-main-row {
  align-items: stretch;
}

.home-card {
  border-radius: 12px;
  border: none;

  ::v-deep .el-card__header {
    padding: 14px 18px;
    border-bottom: 1px solid #eef2f0;
  }

  ::v-deep .el-card__body {
    padding: 18px;
  }
}

.home-card--notice {
  height: 100%;
  min-height: 320px;

  ::v-deep .el-card__body {
    padding: 12px 16px 16px;
  }
}

.home-card__header {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.home-card__header-icon {
  margin-right: 8px;
  font-size: 18px;
  color: #2d8a6e;
}

.home-feature-intro {
  margin: 0 0 18px;
  font-size: 14px;
  line-height: 1.75;
  color: #606266;

  strong {
    color: #1a5f4a;
    font-weight: 600;
  }
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 14px;
}

.feature-tile {
  display: flex;
  align-items: flex-start;
  padding: 14px;
  border-radius: 10px;
  border: 1px solid #ebeef5;
  background: #fafcfb;
  cursor: pointer;
  transition: border-color 0.2s, box-shadow 0.2s, transform 0.15s;

  &:hover {
    border-color: #b7e0d2;
    box-shadow: 0 6px 16px rgba(45, 138, 110, 0.12);
    transform: translateY(-2px);
  }

  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px rgba(45, 138, 110, 0.35);
  }
}

.feature-tile__icon-wrap {
  flex-shrink: 0;
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 22px;
  color: #fff;

  &--green {
    background: linear-gradient(145deg, #2d8a6e, #1a5f4a);
  }
  &--teal {
    background: linear-gradient(145deg, #20b2aa, #17806a);
  }
  &--blue {
    background: linear-gradient(145deg, #409eff, #2d6fd9);
  }
  &--amber {
    background: linear-gradient(145deg, #e6a23c, #c97c1a);
  }
  &--rose {
    background: linear-gradient(145deg, #f56c6c, #c84855);
  }
}

.feature-tile__body {
  min-width: 0;
}

.feature-tile__title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 6px;
}

.feature-tile__desc {
  font-size: 12px;
  line-height: 1.55;
  color: #909399;
  margin-bottom: 8px;
}

.feature-tile__action {
  font-size: 12px;
  color: #2d8a6e;
  font-weight: 500;

  .el-icon-right {
    font-size: 12px;
  }
}

.notice-body {
  min-height: 200px;
}

.notice-table {
  width: 100%;

  ::v-deep .el-table__header th {
    background: #f4faf8 !important;
    color: #606266;
    font-weight: 600;
  }

  ::v-deep .el-table__row:hover > td {
    background: #f8fcfb !important;
  }
}

.notice-date {
  color: #909399;
  font-size: 12px;
}

.notice-content::v-deep img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
}

.notice-dialog-title {
  text-align: center;
  font-weight: 600;
  padding-right: 40px;
}
</style>
