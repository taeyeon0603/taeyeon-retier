<template>
  <div>
    <el-dialog :title="'党支部'" v-model="dialogVisibleRow" :width="1000" @close="handleClose">
      <div id="BarChartsRow1" ref="BarChartsRow1" class="BarChartsRow"></div>
    </el-dialog>
  </div>
</template>

<script>

import { inject, ref, onMounted, reactive, nextTick } from "vue"
import { getDeptDist } from "@/api/screen"
import bus from "@/bus"
import { postRetireList } from '@/api/retiree'
import DialogTable from "@/views/screen/DialogTable"

export default {
  name: "DialogRowBar",
  components: {
    DialogTable,
  },
  setup() {

    let BarChartsRow1 = ref(null)
    let dialogVisibleRow = ref(false)
    const BarEcharts = inject('ec')
    let myColor = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399', '#5f52a0'];
    let dataArr = []
    let nameArr = []
    let baiK = []
    let waiK = []
    let waiY = []
    let title = ref('')
    let tableData = reactive({
      data: []
    })


    function retireeList(partyBranch) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        partyBranch: partyBranch,
        isHistory: false,
      }).then(res => {

        if (res.code == 200) {

          tableData.data = res.data.content
          bus.$emit('retireeListDialog', {
            list: tableData.data,
            title: title.value
          })
        }
      }).catch(err => {
      })
    }
    function getDeptDistData() {

      dataArr = []
      nameArr = []
      baiK = []
      waiK = []
      waiY = []

      getDeptDist().then(res => {
        if (res.code === 200) {
          for (let item of res.data.partyBranch.reverse()) {

            if (item.name != "无") {
              dataArr.push(item.value)
              nameArr.push(item.name)
              baiK.push(99.5)
              waiK.push(100)
              waiY.push(0)
            }
          }
        }

        console.log(nameArr)
        initChart()
      })
    }


    // bus.$on('dialogRowBar', val => {
    //   dialogVisible.value = true
    //   nextTick(() => {
    //     getDeptDistData()
    //   })
    // })

    function initChart() {
      BarEcharts.init(BarChartsRow1.value).dispose()
      let myBarRowChart = BarEcharts.init(BarChartsRow1.value)
      myBarRowChart.on('click', function (params) {

        if (params.name.indexOf("支部") != -1) {
          retireeList(params.name)
          title.value = params.name
        }
      });
      myBarRowChart.setOption({
        backgroundColor: '',
        grid: {
          left: '5%',
          top: '5%',
          right: '0%',
          bottom: '8%',
          containLabel: true
        },
        xAxis: [{
          show: false,
        }],
        yAxis: [{
          axisTick: 'none',
          axisLine: 'none',
          offset: '27',
          axisLabel: {

            color: '#000000',
            fontSize: '16',

          },
          data: nameArr
        }, {
          axisTick: 'none',
          axisLine: 'none',
          axisLabel: {

            color: '#ffffff',
            fontSize: '16',

          },
          data: dataArr
        }, {
          name: '分拨延误TOP 10',
          nameGap: '50',
          nameTextStyle: {
            color: '#ffffff',
            fontSize: '16',
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(0,0,0,0)'
            }
          },
          data: [],
        }],
        series: [{
          name: '条',
          type: 'bar',
          yAxisIndex: 0,
          data: dataArr,
          label: {

            show: true,
            position: 'right',

            color: '#000',
            fontSize: '16',


          },
          barWidth: 16,
          itemStyle: {

            color: function (params) {
              var num = myColor.length;
              return myColor[params.dataIndex % num]
            },

          },
          z: 2
        }, {
          name: '白框',
          type: 'bar',
          yAxisIndex: 1,
          barGap: '-100%',
          data: baiK,
          barWidth: 20,
          itemStyle: {

            color: '#fff',
            borderRadius: 5,

          },
          z: 1
        }, {
          name: '外框',
          type: 'bar',
          yAxisIndex: 2,
          barGap: '-100%',
          data: waiK,
          barWidth: 24,
          itemStyle: {

            color: function (params) {
              var num = myColor.length;
              return myColor[params.dataIndex % num]
            },
            borderRadius: 5,

          },
          z: 0
        },
        {
          name: '外圆',
          type: 'scatter',
          emphasis: { scale: false },
          data: waiY,
          yAxisIndex: 2,
          symbolSize: 30,
          itemStyle: {

            color: function (params) {
              var num = myColor.length;
              return myColor[params.dataIndex % num]
            },
            opacity: 1,

          },
          z: 2
        }
        ]
      })
      window.onresize = function () {
        //自适应大小
        myBarRowChart.resize();
      };
    }

    function handleClose() {
      dialogVisibleRow.value = false
    }
    onMounted(() => {
      bus.$on('dialogRowBar', val => {
        dialogVisibleRow.value = true
        nextTick(() => {
          getDeptDistData()
        })
      })
    })
    return { dialogVisibleRow, handleClose, BarChartsRow1 }
  }
}
</script>

<style scoped lang="scss">
.BarChartsRow {
  width: 100%;
  height: 40vh;
}
</style>