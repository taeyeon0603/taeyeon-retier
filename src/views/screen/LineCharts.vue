<template>
  <div>
    <div id="lineChart" class="lineChart"></div>
  </div>
</template>

<script>

import { inject, ref, onMounted, reactive } from "vue"
import { getActiveDist } from "@/api/screen"
import { show } from "dom7"
import bus from "@/bus"
import { postRetireList } from '@/api/retiree'
import { tsEnumDeclaration } from "@babel/types"

export default {
  name: "LineCharts",
  setup() {

    const lineEchart = inject('ec')
    let dataArr = []
    let dataValue = []
    let title = ref('')
    let tableData = reactive({
      data: []
    })

    function getActiveDistData() {
      getActiveDist().then(res => {
        if (res.code === 200) {
          for (let item of res.data) {
            if (item.name == "1.0") {
              dataArr.push("一星")
              dataValue.push(item.value)
            } else if (item.name == "2.0") {
              dataArr.push("二星")
              dataValue.push(item.value)
            } else if (item.name == "3.0") {
              dataArr.push("三星")
              dataValue.push(item.value)
            } else if (item.name == "4.0") {
              dataArr.push("四星")
              dataValue.push(item.value)
            } else if (item.name == "5.0") {
              dataArr.push("五星")
              dataValue.push(item.value)
            }
          }
        }
        initChart()
      })
    }
    //获取列表
    function retireeList(value) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        activeRate: value,
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

    function initChart() {
      lineEchart.init(document.getElementById('lineChart')).dispose()
      let myLineChart = lineEchart.init(document.getElementById('lineChart'))
      myLineChart.on('click', function (params) {
        if (params.name === '一星') {
          retireeList(1)
        } else if (params.name === '二星') {
          retireeList(2)
        } else if (params.name === '三星') {
          retireeList(3)
        } else if (params.name === '四星') {
          retireeList(4)
        } else if (params.name === '五星') {
          retireeList(5)
        }
        title.value = params.name
      });
      myLineChart.setOption({
        backgroundColor: '',
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0,
                  color: 'rgba(0, 255, 233,0)'
                }, {
                  offset: 0.5,
                  color: 'rgba(255, 255, 255,1)',
                }, {
                  offset: 1,
                  color: 'rgba(0, 255, 233,0)'
                }],
                global: false
              }
            },
          },
        },
        grid: {
          top: '8%',
          bottom: '12%',
          // containLabel: true
        },
        xAxis: [{
          type: 'category',
          axisLine: {
            show: true
          },
          splitArea: {
            // show: true,
            color: '#f00',
            lineStyle: {
              color: '#f00'
            },
          },
          axisLabel: {
            color: '#fff'
          },
          splitLine: {
            show: false
          },
          boundaryGap: false,
          data: dataArr,

        }],

        yAxis: [{
          type: 'value',
          min: 0,
          max: 150,
          splitNumber: 4,
          splitLine: {
            show: true,
            lineStyle: {
              color: 'rgba(255,255,255,0.1)'
            }
          },
          axisLine: {
            show: true,
          },
          axisLabel: {
            show: true,
            margin: 20,
            color: '#d1e6eb',

          },
          axisTick: {
            show: false,
          },
        }],
        series: [
          {
            name: '注册总量',
            type: 'line',
            // smooth: true, //是否平滑
            showAllSymbol: true,
            // symbol: 'image://./static/images/guang-circle.png',
            symbol: 'circle',
            symbolSize: 10,
            lineStyle: {

              color: "#00ca95",
              shadowColor: 'rgba(0, 0, 0, .3)',
              shadowBlur: 0,
              shadowOffsetY: 5,
              shadowOffsetX: 5,

            },
            label: {
              show: true,
              position: 'top',
              color: '#00ca95',

            },

            itemStyle: {
              color: "#00ca95",
              borderColor: "#fff",
              borderWidth: 3,
              shadowColor: 'rgba(0, 0, 0, .3)',
              shadowBlur: 0,
              shadowOffsetY: 2,
              shadowOffsetX: 2,
            },
            tooltip: {
              show: false
            },
            areaStyle: {

              color: new lineEchart.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(0,202,149,0.3)'
              },
              {
                offset: 1,
                color: 'rgba(0,202,149,0)'
              }
              ], false),
              shadowColor: 'rgba(0,202,149, 0.9)',
              shadowBlur: 20

            },
            data: dataValue,
          },
        ]
      })
      window.onresize = function () {
        //自适应大小
        myLineChart.resize();
      };
    }


    onMounted(() => {

      getActiveDistData()
    })


    return {}
  }
}
</script>

<style scoped lang="scss">
.lineChart {
  width: 100%;
  height: calc(18vh);

}
</style>