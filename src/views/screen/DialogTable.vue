<template>
  <div style="" class="deep">
    <el-dialog :title="title" v-model="dialogVisible" :width="1200" @close="handleClose">
      <el-table :data="tableData.data" height="500" border style="width: 100%">
        <el-table-column type="index"></el-table-column>
        <el-table-column prop="name" label="姓名" width="80">
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="80">
        </el-table-column>
        <el-table-column prop="birth" label="出生年月" width="120" :formatter="formatBirth">
        </el-table-column>

        <el-table-column v-if="!isAdmin" prop="residenceAddr" label="实际居住地">
        </el-table-column>

        <el-table-column v-else prop="idCard" label="身份证号">
        </el-table-column>
        <el-table-column prop="politicalIdentity" width="80" label="政治面貌">
        </el-table-column>
        <el-table-column prop="tel" width="110" label="手机号码">
        </el-table-column>
        <el-table-column prop="policeStation" width="95" label="辖区居住地">
        </el-table-column>
        <el-table-column prop="community" width="100" label="社区联系电话">
        </el-table-column>
        <el-table-column prop="police" width="80" label="社区负责人">
        </el-table-column>
        <el-table-column prop="party" width="120" label="退休党支部">
        </el-table-column>
        <el-table-column prop="deptName" width="140" label="原所在单位">
        </el-table-column>
        <el-table-column prop="retireDate" width="120" label="退休时间" :formatter="formatRetireDate">
        </el-table-column>
        <el-table-column v-if="isAdmin" label="" width="100" key="operation">
          <template #default="scope">
            <el-button type="text" class="customText" size="small" @click="showRetireeDetail(scope.row)">详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <retiree-detail></retiree-detail>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, inject, provide, nextTick } from 'vue'
import moment from 'moment'
import RetireeDetail from "@/components/retiree/RetireeDetail"
import bus from "@/bus"
import { useStore } from 'vuex'

// import {getColorData} from '@/api/getMapDataJson'
// import {getYearSelectItem} from '@/api/getMapDataJson'

// import {getCommunityScore} from "@/api/index"
// import {getPcsScore} from "@/api/index"
// import {getYearItem} from "@/api/index"


export default {
  name: "DialogTable",
  components: {
    RetireeDetail,
  },
  setup() {
    let store = useStore()
    let username = store.state.user.userInfo.name
    let isAdmin = ref(false)
    for (let item of store.state.user.userInfo.roles) {
      if (item.name == '超级管理员' || item.name == '机关党委') {
        isAdmin.value = true
      }
    }

    let title = ref('')
    let dialogVisible = ref(false)
    let tableData = reactive({
      data: []
    })
    let retireeDetailVisible = ref(false)
    provide('retireeDetailVisible', retireeDetailVisible)

    function showRetireeDetail(val) {
      retireeDetailVisible.value = true
      bus.$emit('retireeDetailDialog', val)
    }
    //获取列表


    bus.$on('retireeListDialog', val => {

      dialogVisible.value = true
      tableData.data = val.list
      title.value = val.title

    })

    function handleClose() {
      dialogVisible.value = false
    }

    function formatRetireDate(val) {
      if (val.retireDate) {
        return moment(val.retireDate).format("YYYY-MM-DD")
      } else {
        return ""
      }
    }

    function formatBirth(val) {
      if (val.birth) {
        return moment(val.birth).format("YYYY-MM-DD")
      } else {
        return ""
      }
    }
    onMounted(() => {
    })
    return { title, dialogVisible, handleClose, formatRetireDate, tableData, showRetireeDetail, username, isAdmin, formatBirth }

  }

}
</script>


<style scoped lang="scss">
:deep(.el-table th) {
  background-color: #efefef;
}

:deep(.el-table td) {
  padding: 8px 0px;
}

:deep(.el-table__header-wrapper) {

  border-bottom: 1px solid #d2d2d2 !important;
  background-color: #efefef;
}

:deep(.el-table) {

  div::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }

  div::-webkit-scrollbar-thumb {
    background-color: #9c9c9c;
  }
}

.border-box-content {
  background-color: #001a33;
  display: flex;
  align-items: center;
  justify-content: center;
}

.deep>>>.el-table .yellow-row {
  background: #fdf4b6;
}

.deep>>>.el-table .green-row {
  background: #b8f99a;
}

.deep>>>.el-table .red-row {
  background: #f9a1a1;
}

.deep>>>.el-table .blue-row {
  background: #a7d9f9;
}

.deep>>>.tableUl {
  width: 100%;
  list-style-type: none;
  padding: 0;

}

.deep>>>.tableUl li {
  width: 99px;
  float: left;
  border-left: 1px solid #EBEEF5;
  border-top: 1px solid #EBEEF5;
  border-bottom: 1px solid #EBEEF5;
  height: 48px;
  background: #a2a2a2;
  text-indent: 10px;
  line-height: 48px;
  color: #ffffff;

}

.deep>>>.tableUl li:last-child {
  width: 98px;
  border-right: 1px solid #EBEEF5;
}

.deep>>>.tableUl li:first-child {
  width: 120px;
  background: none;
  border: none;
  text-indent: 0px;
  text-align: center;
  color: #606266;
  font-weight: bold;

}

.deep>>>.el-table__expanded-cell {
  padding: 0px 0px 20px 112px;
}

.map-top {
  right: 25px;
  top: 5px;
  /*width: 400px;*/
  height: 30%;
  width: 120px;
}

.map-zn {
  position: absolute;
  /*background: rgba(0, 43, 255, 0.51);*/
  z-index: 99;
  border-radius: 5px;
}

.deep>>>.el-select-dropdown__item {
  background: rgba(19, 90, 171, 0.13);
}

.deep>>>.el-input__inner {
  background-color: transparent;
  border: 1px solid #3a8ee6;
  color: #00f6ff;
}

.deep>>>.el-select-dropdown__item {
  background-color: transparent;
  color: #3f9fd8;
}

.deep>>>.el-select-dropdown {
  background-color: transparent;
  border: 1px solid #3a8ee6;
  left: 0px !important;
}

.deep>>>.selected {
  color: #00f6ff
}

.deep>>>.popper__arrow {
  border-bottom-color: #3a8ee6;
  top: -7px;
}

.deep>>>.popper__arrow:after {
  border-bottom-color: #3a8ee6
}

.deep>>>div::-webkit-scrollbar {
  width: 6px;
}


.deep>>>div::-webkit-scrollbar-thumb {
  background-color: rgba(58, 201, 217, 0.5);
}
</style>
