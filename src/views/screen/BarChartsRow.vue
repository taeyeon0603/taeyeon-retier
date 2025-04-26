<template>
  <div>
    <div id="BarChartsRow1" class="BarChartsRow"></div>
  </div>
</template>

<script>

import { inject, ref, onMounted } from "vue"
import { getDeptDist } from "@/api/screen"

export default {
  name: "LineCharts",
  setup() {

    const BarEchart = inject('ec')
    let myColor = ['#eb3600', '#d0a00e', '#00e9db', '#00c0e9', '#33FFCC'];
    let dataArr = []
    let nameArr = []


    function getDeptDistData() {
      getDeptDist().then(res => {
        if (res.code === 200) {
          for (let item of res.data.partyBranch) {
            if (item.name != "货物运输有限公司党支部") {
              dataArr.push(item.value)
              nameArr.push(item.name)
            }
          }
        }
        initChart()
      })
    }

    function initChart() {
      BarEchart.init(document.getElementById('BarChartsRow1')).dispose()
      let myBarRowChart = BarEchart.init(document.getElementById('BarChartsRow1'))
      myBarRowChart.setOption({
        backgroundColor: '',
        grid: {
          left: '8%',
          top: '5%',
          right: '8%',
          bottom: '1%',
          containLabel: true
        },
        xAxis: [{
          show: false,
        }],
        yAxis: [{
          axisTick: 'none',
          axisLine: 'none',
          offset: '20',
          axisLabel: {
            textStyle: {
              color: '#ffffff',
              fontSize: '14',
            }
          },
          data: nameArr
        }, {
          axisTick: 'none',
          axisLine: 'none',
          axisLabel: {
            textStyle: {
              color: '#ffffff',
              fontSize: '0',
            }
          },
          data: [10, 9, 8, 7, 6, 5]
        }, {
          name: '工作地TOP 10',
          nameGap: '30',
          nameTextStyle: {
            color: '#ffffff',
            fontSize: '20',
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
            textStyle: {
              color: '#ffffff',
              fontSize: '16',
            }

          },
          barWidth: 10,
          itemStyle: {

            color: function (params) {
              let num = myColor.length;
              return myColor[params.dataIndex % num]
            },

          },
          z: 2
        },
        {
          name: '外圆',
          type: 'scatter',
          hoverAnimation: false,
          data: [0, 0, 0, 0, 0, 0],
          yAxisIndex: 2,
          symbolSize: 25,
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

    onMounted(() => {
      getDeptDistData()
    })
    return {}
  }
}
</script>

<style scoped lang="scss">
.pop-content {
  font-size: 16px;
  font-weight: bold;
  line-height: 3vh;
}

.BarChartsRow {
  width: 100%;
  height: 27vh;
}
</style>