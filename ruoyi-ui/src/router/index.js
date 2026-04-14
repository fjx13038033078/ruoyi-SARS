import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 鐠侯垳鏁遍柊宥囩枂妞?
 *
 * hidden: true                     // 瑜版捁顔曠純?true 閻ㄥ嫭妞傞崐娆掝嚉鐠侯垳鏁辨稉宥勭窗閸愬秳鏅舵潏瑙勭埉閸戣櫣骞?婵?01閿涘ogin缁涘銆夐棃顫礉閹存牞鈧懎顩ф稉鈧禍娑氱椽鏉堟垿銆夐棃?edit/1
 * alwaysShow: true                 // 瑜版挷缍樻稉鈧稉顏囩熅閻㈠彉绗呴棃銏㈡畱 children 婢圭増妲戦惃鍕熅閻㈠崬銇囨禍?娑擃亝妞傞敍宀冨殰閸斻劋绱伴崣妯诲灇瀹撳苯顨滈惃鍕佸?-婵″倻绮嶆禒鍫曘€夐棃?
 *                                  // 閸欘亝婀佹稉鈧稉顏呮閿涘奔绱扮亸鍡涘亝娑擃亜鐡欑捄顖滄暠瑜版挸浠涢弽纭呯熅閻㈣鲸妯夌粈鍝勬躬娓氀嗙珶閺?-婵″倸绱╃€靛ジ銆夐棃?
 *                                  // 閼汇儰缍橀幆鍏呯瑝缁犅ょ熅閻㈠彉绗呴棃銏㈡畱 children 婢圭増妲戦惃鍕嚋閺佷即鍏橀弰鍓с仛娴ｇ姷娈戦弽纭呯熅閻?
 *                                  // 娴ｇ姴褰叉禒銉啎缂?alwaysShow: true閿涘矁绻栭弽宄扮暊鐏忓彉绱拌箛鐣屾殣娑斿澧犵€规矮绠熼惃鍕潐閸掓瑱绱濇稉鈧惄瀛樻▔缁€鐑樼壌鐠侯垳鏁?
 * redirect: noRedirect             // 瑜版捁顔曠純?noRedirect 閻ㄥ嫭妞傞崐娆掝嚉鐠侯垳鏁遍崷銊╂桨閸栧懎鐫夌€佃壈鍩呮稉顓濈瑝閸欘垵顫﹂悙鐟板毊
 * name:'router-name'               // 鐠佹儳鐣剧捄顖滄暠閻ㄥ嫬鎮曠€涙绱濇稉鈧€规俺顩︽繅顐㈠晸娑撳秶鍔ф担璺ㄦ暏<keep-alive>閺冩湹绱伴崙铏瑰箛閸氬嫮顫掗梻顕€顣?
 * query: '{"id": 1, "name": "ry"}' // 鐠佸潡妫剁捄顖滄暠閻ㄥ嫰绮拋銈勭炊闁帒寮弫?
 * roles: ['admin', 'common']       // 鐠佸潡妫剁捄顖滄暠閻ㄥ嫯顫楅懝鍙夋綀闂?
 * permissions: ['a:a:a', 'b:b:b']  // 鐠佸潡妫剁捄顖滄暠閻ㄥ嫯褰嶉崡鏇熸綀闂?
 * meta : {
    noCache: true                   // 婵″倹鐏夌拋鍓х枂娑撶皪rue閿涘苯鍨稉宥勭窗鐞?<keep-alive> 缂傛挸鐡?姒涙顓?false)
    title: 'title'                  // 鐠佸墽鐤嗙拠銉ㄧ熅閻㈠崬婀笟褑绔熼弽蹇撴嫲闂堛垹瀵樼仦鎴滆厬鐏炴洜銇氶惃鍕倳鐎?
    icon: 'svg-name'                // 鐠佸墽鐤嗙拠銉ㄧ熅閻㈣京娈戦崶鐐垼閿涘苯顕惔鏃囩熅瀵板墕rc/assets/icons/svg
    breadcrumb: false               // 婵″倹鐏夌拋鍓х枂娑撶alse閿涘苯鍨稉宥勭窗閸︹暈readcrumb闂堛垹瀵樼仦鎴滆厬閺勫墽銇?
    activeMenu: '/system/user'      // 瑜版捁鐭鹃悽杈啎缂冾喕绨＄拠銉ョ潣閹嶇礉閸掓瑤绱版妯瑰瘨閻╃顕惔鏃傛畱娓氀嗙珶閺嶅繈鈧?
  }
 */

// ...
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: 'Home', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: '娑擃亙姹夋稉顓炵妇', icon: 'user' }
      }
    ]
  }
]

// ...
export const dynamicRoutes = [
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/authRole'),
        name: 'AuthRole',
        meta: { title: 'Deploy Visualize', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/system/role/authUser'),
        name: 'AuthUser',
        meta: { title: 'Deploy Visualize', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/system/dict/data'),
        name: 'Data',
        meta: { title: 'Dict Data', activeMenu: '/system/dict' }
      }
    ]
  },
  {
    path: '/agricultural/deploy-detail',
    component: Layout,
    hidden: true,
    permissions: ['agricultural:deploy:visualize'],
    children: [
      {
        path: 'visualize/:schemeId(\\d+)',
        component: () => import('@/views/agricultural/deploy/visualize'),
        name: 'DeployVisualize',
        meta: { title: 'Deploy Visualize', activeMenu: '/agricultural/deploy' }
      }
    ]
  },
]

// Prevent duplicate route push errors
let routerPush = Router.prototype.push;
let routerReplace = Router.prototype.replace;
// push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
Router.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
