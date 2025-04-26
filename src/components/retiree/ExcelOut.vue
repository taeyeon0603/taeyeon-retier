<template>
  <div class="custom-add">
    <el-dialog title="excel导出" v-model="excelDialogVisible" ref="drawer" :width="400">
      <template #title>
        <el-row>
          <el-col :span="8" style="font-size: 22px;font-weight: bold;color: #d83b51;">excel导出</el-col>
          <el-col :span="12">
            <el-checkbox style="margin-top: 10px;" v-model="allCheckVal" @change="allCheck" :label="true">全选
            </el-checkbox>
          </el-col>
        </el-row>
      </template>
      <el-row class="checkbox-row">
        <el-col :span="24" style="text-align: left">
          <el-checkbox-group v-model="checkList" @change="checkboxChange">
            <ul class="out-ul">
              <li v-for="itemChildren in formsAll.data">
                <el-checkbox :label="itemChildren.key">{{ itemChildren.title }}</el-checkbox>
              </li>
            </ul>
          </el-checkbox-group>
        </el-col>
      </el-row>
      <template #footer>
        <div class="demo-drawer__footer">
          <el-button class="el-button--custom" @click="handleOut" :loading="formLoading">{{
              formLoading ?
                '提交中...' : '确定'
          }}
          </el-button>
          <el-button class="el-button-white" @click="cancelForm">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, inject, nextTick, onMounted } from 'vue'
import { postRetireAllList, postRetireAllNewList, postRetireAllExtraList } from '@/api/retiree'
import { getRoleByUser, userBindRole } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import bus from "@/bus"
import table2excel from 'js-table2excel'
import moment from "moment";

export default {
  name: "Empower",
  setup() {

    let type = inject("type")


    let formsAll = reactive({
      data: [
        {
          title: "照片",
          key: "pic",
          type: 'image',
          width: 108,
          height: 144
        },
        {
          title: "姓名",
          key: "name",
          type: "text"
        },
        {
          title: "年龄",
          key: "age",
          type: "text"
        },
        {
          title: "出生年月",
          key: "birth",
          type: "text"
        },
        {
          title: "党龄",
          key: "partyAge",
          type: "text"
        },
        {
          title: "性别",
          key: "gender",
          type: "text"
        },

        {
          title: "身份证号",
          key: "idCard",
          type: "text"
        },

        {
          title: "手机号码",
          key: "tel",
          type: "text"
        },
        {
          title: "政治面貌",
          key: "politicalIdentity",
          type: "text"
        },
        {
          title: "入党时间",
          key: "joinPartyDate",
          type: "text"
        },

        {
          title: "短号",
          key: "shortTel",
          type: "text"
        },

        {
          title: "退休时间",
          key: "retireDate",
          type: "text"
        },


        {
          title: "实际居住地",
          key: "residenceAddr",
          type: "text"
        },

        {
          title: "居住地类型",
          key: "residenctType",
          type: "text"
        },

        {
          title: "所属支部",
          key: "party",
          type: "text"
        },
        {
          title: "原单位",
          key: "deptName",
          type: "text"
        },
        {
          title: "原职级",
          key: "level",
          type: "text"
        },


        {
          title: "原职务",
          key: "post",
          type: "text"
        },
        {
          title: "疾病",
          key: "disease",
          type: "text"
        },
        {
          title: "银行卡号",
          key: "pensionBankCard",
          type: "text"
        },

        {
          title: "健康状况",
          key: "healthStatus",
          type: "text"
        },
        {
          title: "家庭情况",
          key: "familyCondition",
          type: "text"
        },
        {
          title: "生活困难",
          key: "isLifeHard",
          type: "text"
        },

        {
          title: "文化活动",
          key: "activities",
          type: "text"
        },

        {
          title: "疫苗接种",
          key: "vaccinationRecords",
          type: "text"
        },
        {
          title: "家属联系方式",
          key: "familyTel",
          type: "text"
        },

        {
          title: "老干部活动",
          key: "activitiesRecords",
          type: "text"
        },
        {
          title: "其他",
          key: "remarkRecords",
          type: "text"
        },
        {
          title: "派出所走访",
          key: "visitRecords",
          type: "text"
        },
        {
          title: "孤寡老人",
          key: "isLonelyOldMan",
          type: "text"
        },
        {
          title: "居住辖区",
          key: "policeStation",
          type: "text"
        },
        {
          title: "所属社区",
          key: "community",
          type: "text"
        },

        {
          title: "社区负责人",
          key: "police",
          type: "text"
        },
        {
          title: "社区联系电话",
          key: "policeTel",
          type: "text"
        },

        {
          title: "备注",
          key: "remark",
          type: "text"
        },

        {
          title: "历史归档",
          key: "isHistory",
          type: "text"
        },
      ]
    })


    let forms = ref(null)
    let drawerTitle = ref('excel导出')
    let excelDialogVisible = inject('excelDialogVisible')
    let formLoading = ref(false)
    let userId = ref(0)
    let tableData = reactive({
      data: []
    })
    let tableParam = reactive({
      dept: "",
      idCard: "",
      isHistory: false,
      name: ""
    })

    let checkList = ref([])
    let allCheckVal = ref(false)
    const allCheck = (() => {
      if (allCheckVal.value === true) {
        for (let item of formsAll.data) {
          checkList.value.push(item.key)
        }
      } else {
        checkList.value.splice(0, checkList.value.length)
      }
    })

    bus.$on('excelDialogVisible', (val) => {
      userId.value = val.id

      getRoleByUser(val.id).then(res => {
        if (res.code === 200) {
          checkList.value = []
          for (let item of res.data) {
            checkList.value.push(item.id)
          }
        }
      }).catch(err => {

      })
      console.log(val)
    })


    function handleOut() {
      let date = new Date()
      let excelName = "结果导出" + date.format("yyyyMMddhhmmss")

      let excelHeader = []
      for (let item of checkList.value) {
        for (let itemAll of formsAll.data) {
          if (itemAll.key === item) {
            excelHeader.push(itemAll)
          }
        }
      }
      table2excel(excelHeader, tableData.data, excelName)
      ElMessage.success('操作成功！')
      excelDialogVisible.value = false
    }

    function checkboxChange() {
      console.log(checkList.value)
    }

    function cancelForm() {
      excelDialogVisible.value = false
    }

    onMounted(() => {
      if (type === "list") {
        postRetireAllList(tableParam).then(res => {
          if (res.code === 200) {
            // bus.$emit('refreshTableData')
            console.log(res.data)
            res.data.map(item => {
              item.birth = item.birth != null ? moment(item.birth).format("YYYY-MM-DD") : ""
              item.joinPartyDate = item.joinPartyDate != null ? moment(item.joinPartyDate).format("YYYY-MM-DD") : ""
              item.retireDate = item.retireDate != null ? moment(item.retireDate).format("YYYY-MM-DD") : ""
              item.moveInAt = item.moveInAt != null ? moment(item.moveInAt).format("YYYY-MM-DD") : ""
              item.moveOutAt = item.moveOutAt != null ? moment(item.moveOutAt).format("YYYY-MM-DD") : ""
              item.idCard = item.idCard + "'"
              return item
            })
            tableData.data = res.data
          }
        }).catch(err => {
        })
      } else if (type === "new") {
        postRetireAllNewList(tableParam).then(res => {
          if (res.code === 200) {
            // bus.$emit('refreshTableData')
            console.log(res.data)
            res.data.map(item => {
              item.birth = item.birth != null ? moment(item.birth).format("YYYY-MM-DD") : ""
              item.joinPartyDate = item.joinPartyDate != null ? moment(item.joinPartyDate).format("YYYY-MM-DD") : ""
              item.retireDate = item.retireDate != null ? moment(item.retireDate).format("YYYY-MM-DD") : ""
              item.moveInAt = item.moveInAt != null ? moment(item.moveInAt).format("YYYY-MM-DD") : ""
              item.moveOutAt = item.moveOutAt != null ? moment(item.moveOutAt).format("YYYY-MM-DD") : ""
              item.idCard = item.idCard + "'"
              return item
            })
            tableData.data = res.data
          }
        }).catch(err => {
        })
      } else {
        postRetireAllExtraList(tableParam).then(res => {
          if (res.code === 200) {
            // bus.$emit('refreshTableData')
            console.log(res.data)
            res.data.map(item => {
              item.birth = item.birth != null ? moment(item.birth).format("YYYY-MM-DD") : ""
              item.joinPartyDate = item.joinPartyDate != null ? moment(item.joinPartyDate).format("YYYY-MM-DD") : ""
              item.retireDate = item.retireDate != null ? moment(item.retireDate).format("YYYY-MM-DD") : ""
              item.moveInAt = item.moveInAt != null ? moment(item.moveInAt).format("YYYY-MM-DD") : ""
              item.moveOutAt = item.moveOutAt != null ? moment(item.moveOutAt).format("YYYY-MM-DD") : ""
              item.idCard = item.idCard + "'"
              return item
            })
            tableData.data = res.data
          }
        }).catch(err => {
        })
      }
    })
    return {
      forms,
      drawerTitle,
      excelDialogVisible,
      formLoading,
      handleOut,
      cancelForm,
      checkboxChange,
      checkList,
      formsAll,
      allCheck,
      allCheckVal
    }
  }
}
</script>

<style scoped lang="scss">
.custom-add {
  text-align: left;

  .out-ul {
    margin: 0px;
    padding: 0px;

    li {
      list-style: none;
      float: left;
      width: 33%;
      height: 30px;
    }
  }

  :deep(.el-dialog__title) {
    color: $font-color-theme;
    font-weight: bold;
  }

  :deep(.el-dialog__body) {
    padding-top: 0px;
    padding-bottom: 10px;
  }

  :deep(.el-form-item__content) {
    text-align: left;
  }

  :deep(.el-input__inner) {
    border: 1px solid #757575;
  }


  :deep(.el-divider--horizontal) {
    margin: 8px 0px 18px 0px;
    background-color: #636363;
  }
}

.checkbox-row {
  margin-top: 20px;


  .content {
    margin-top: 10px;
    margin-left: 20px;
    text-align: left;
  }


  :deep(.el-checkbox__inner::after) {
    border-color: #ffffff;
  }

}
</style>