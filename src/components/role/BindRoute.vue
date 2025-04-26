<template>
  <div class="custom-add">
    <el-drawer title="绑定路由" v-model="routeDrawer" direction="rtl" ref="drawer" :size="400">
      <el-row>
        <el-col :span="24">
          <el-tree ref="tree" :data="routeData.data" show-checkbox default-expand-all node-key="id" highlight-current
            @change="currentChange" :check-strictly="systemNodeFlag" />
        </el-col>
      </el-row>


      <el-divider content-position="left"></el-divider>
      <div class="demo-drawer__footer">

        <el-button class="el-button--custom" @click="submitAdd" :loading="formLoading">{{ formLoading ?
            '提交中...' : '确定'
        }}
        </el-button>
        <el-button class="el-button-white" @click="cancelForm">取消</el-button>
      </div>

    </el-drawer>
  </div>
</template>

<script>
import { ref, reactive, inject, nextTick, onMounted } from 'vue'
import { getAllRouterList, getRoleRouterById, routerBind } from '@/api/router'
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"

export default {
  name: "BindRoute",
  setup() {
    let forms = ref(null)
    let routeDrawer = inject('routeDrawer')
    let formLoading = ref(false)
    let routeData = reactive({
      data: []
    })
    let checkIds = ref(['7739da4b-da13-4bd0-b541-873f654ca81c'])
    let roleId = ref(0)
    let tree = ref(null)
    let systemNodeFlag = ref(false)


    bus.$on('handleBindRoute', (val) => {
      roleId.value = val.id
      getAllRouter()
    })


    function getRouterById() {
      checkIds.value = ['7739da4b-da13-4bd0-b541-873f654ca81c']
      getRoleRouterById(roleId.value).then(res => {
        let routeDataById = []
        if (res.code === 200) {

          for (let item of res.data) {
            for (let itemChildren of item.children) {
              routeDataById.push({
                id: itemChildren.id,
                label: itemChildren.meta.title
              })
            }
            routeDataById.push({
              id: item.id,
              label: item.meta.title
            })
          }

          systemNodeFlag.value = true
          nextTick(() => {
            tree.value.setCheckedNodes(routeDataById)
            systemNodeFlag.value = false
          })
          for (let item of tree.value.getCheckedNodes(false, true)) {
            checkIds.value.push(item.id)
          }
        }
      }).catch(err => {
      })
    }

    function getAllRouter() {
      let childrenData = []
      routeData.data = []
      getAllRouterList().then(res => {
        if (res.code === 200) {
          for (let item of res.data) {
            for (let itemChildren of item.children) {
              childrenData.push({
                id: itemChildren.id,
                label: itemChildren.meta.title,
                children: []
              })
            }
            routeData.data.push({
              id: item.id,
              label: item.meta.title,
              children: childrenData,
            })
            childrenData = []
          }

          getRouterById()
          // console.log(routeData.data)
        }
      }).catch(err => {

      })
    }

    function submitAdd() {
      ElMessageBox.confirm('确定提交吗?', '提示', {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        routerBind(roleId.value, { routerIds: checkIds.value }).then(res => {
          if (res.code === 200) {
            ElMessage.success('操作成功')
            routeDrawer.value = false
          }
        }).catch(err => {
        })

      })
    }

    function currentChange(current, node) {
      // console.log(tree.value.getCheckedNodes(false, true))
      checkIds.value = ['7739da4b-da13-4bd0-b541-873f654ca81c']
      for (let item of tree.value.getCheckedNodes(false, true)) {
        checkIds.value.push(item.id)
      }
      // console.log(current)
    }

    function cancelForm() {
      routeDrawer.value = false
    }

    onMounted(() => {
    })


    return {
      forms,
      routeDrawer,
      routeData,
      formLoading,
      submitAdd,
      cancelForm,
      currentChange,
      tree,
      systemNodeFlag
    }
  }
}
</script>

<style scoped lang="scss">
.custom-add {


  :deep(.el-form-item__content) {
    text-align: left;

  }


  :deep(.el-drawer__header span) {
    outline: none;
    color: $font-color-theme;
    font-size: 20px;
    font-weight: bold;
    text-align: left;
  }

  :deep(.el-divider--horizontal) {
    margin: 8px 0px 18px 0px;
  }

  :deep(.el-tree) {
    margin-left: 30px;

    .el-tree-node__expand-icon {
      font-size: 18px;
    }

    .el-checkbox__inner {
      width: 16px;
      height: 16px;
    }

    .el-checkbox__inner::after {
      left: 5px;
      top: 2px;
    }

    .el-tree-node__label {
      font-size: 16px;
      color: $tree-father-node-color;
      font-weight: bold;
    }

    .el-tree-node__content {
      margin-bottom: 7px;
    }

    .el-tree-node__children {
      .el-tree-node__label {
        color: #606266 !important;
        font-weight: initial;
        font-size: 14px;

      }
    }

  }

}
</style>