<template>
  <div class="list-contain">

    <div class="search">
      <ul>
        <li>
          <div class="label">标题</div>
          <div class="item">
            <el-input v-model="searchForm.title" placeholder="请输入..."></el-input>
          </div>
        </li>
        <li>
          <div class="label">内容</div>
          <div class="item">
            <el-input v-model="searchForm.content" placeholder="请输入..."></el-input>
          </div>
        </li>
        <li>
          <el-button style="margin-left: 15px" size="medium" class="el-button--custom" @click="onsubmitSearch">查询</el-button>
        </li>
      </ul>
    </div>
    <el-row>
      <el-col :span="24" style="text-align: center;">
        <custom-table></custom-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {ref, reactive, provide, inject} from 'vue'
import CustomTable from "../../components/work/newsList/CustomTable"
import bus from "@/bus"


export default {
  name: "newsList",
  components: {
    CustomTable
  },
  setup() {

    //查询模块
    let searchForm = reactive({
      title: "",
      content:""
    })

    function onsubmitSearch() {
      bus.$emit('handleSearchNews', searchForm)
    }

    return {
      searchForm,
      onsubmitSearch
    }
  }
}
</script>

<style scoped lang="scss">


.list-contain {
  /*height: calc(100% - 40px);*/
  background-color: white;
  margin: 15px 20px 15px 20px;
  padding: 15px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .12), 0 0 6px 0 rgba(0, 0, 0, .04);

  .search {
    height: 60px;

    ul {
      list-style: none;
      padding: 0px;
      margin: 5px;

      li {
        float: left;
        width: calc(25% - 10px);
        margin-right: 10px;
        height: 40px;
        margin-bottom: 5px;
        line-height: 40px;

        .label {
          width: 80px;
          line-height: 40px;
          height: 40px;
          float: left;
          text-align: right;
          color: #383838;
          font-size: 13px;
        }

        .item {
          float: left;
          margin-left: 10px;
          width: calc(100% - 90px);
          text-align: left;


          :deep( .el-range-editor.el-input__inner) {
            width: 100%;

            .el-range-separator {
              line-height: 22px;
            }

          }


          :deep( .el-select) {
            width: 100%;

          }

          :deep( .el-input__inner) {
            height: 30px;
            line-height: 30px;

          }

        }
      }
    }

  }
}


</style>