<template>
  <div>
    <div class="title">
      <el-tooltip effect="customized" placement="top-start">
        <template #content>
          <p class="pop-content">
            原所在单位：退休人员退休时原部门所属现XX“三定”部门名称。因机构改革、更名等原因退休人员退休时原部门现在不存在、更名等的，按原部门业务延续或者关联原则确定现XX“三定”部门名称。
          </p>

        </template>
        <div class="font">原所在单位</div>
      </el-tooltip>

      <div class="buttonDiv">
        <el-button type="text" @click="changeBarData(1)" :class="{ 'button-disable': isPcsBarData === 1 }"
          :disabled="isPcsBarData === 1">职能部门
        </el-button>
        <el-button type="text" @click="changeBarData(2)" :class="{ 'button-disable': isPcsBarData === 2 }"
          :disabled="isPcsBarData === 2">基层部门
        </el-button>
      </div>

    </div>
    <div id="barChart" class="barChart"></div>
  </div>
</template>

<script>

import { inject, ref, onMounted, reactive } from "vue"
import { getDeptDist } from "@/api/screen"
import bus from "@/bus"
import { postRetireList } from '@/api/retiree'
import DialogTable from "@/views/screen/DialogTable"
import { functionsIn } from "lodash"

export default {
  name: "BarCharts",
  components: {
    DialogTable,
  },
  setup() {

    const barEchart = inject('ec')
    let isPcsBarData = ref(1)

    let xData = ref([])
    let yData = ref([])
    let resData = reactive({
      data: null
    })
    let title = ref('')
    let tableData = reactive({
      data: []
    })

    function sortId(a, b) {
      return b.value - a.value
    }

    function changeBarData(num) {
      isPcsBarData.value = num

      xData.value = []
      yData.value = []
      let array = []
      if (isPcsBarData.value === 1) {
        array = JSON.parse(JSON.stringify(resData.data.functionalBrigade))
        for (let item of resData.data.functionalBrigade) {
          xData.value.push(item.name)
          yData.value.push(item.value)
        }
      } else {
        array = JSON.parse(JSON.stringify(resData.data.policeStation))
        for (let item of resData.data.policeStation) {
          xData.value.push(item.name)
          yData.value.push(item.value)
        }
      }
      array.sort(sortId)
      initChart(array[0].value)
    }
    function retireeList(dept) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        dept: dept,
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


    Array.prototype.bubbleSort = function () {
      for (let i = 0; i < this.length - 1; i += 1) {
        for (let j = 0; j < this.length - 1 - i; j += 1) {
          if (this[j].value < this[j + 1].value) {
            const temp = this[j];
            this[j] = this[j + 1];
            this[j + 1] = temp;
          }
        }
      }
    }

    function getDeptData() {
      getDeptDist().then(res => {
        if (res.code === 200) {
          resData.data = res.data
          let array = JSON.parse(JSON.stringify(resData.data.functionalBrigade))

          resData.data.functionalBrigade.bubbleSort()
          resData.data.policeStation.bubbleSort()
          for (let item of resData.data.functionalBrigade) {
            xData.value.push(item.name)
            yData.value.push(item.value)
          }
          array.sort(sortId)
          initChart(array[0].value)
        }
      })
    }


    function initChart(valueMax) {

      barEchart.init(document.getElementById('barChart')).dispose()

      let myBarChart = barEchart.init(document.getElementById('barChart'))
      myBarChart.on('click', function (params) {
        retireeList(params.name)
        title.value = params.name
      });
      myBarChart.setOption({
        backgroundColor: "",
        color: ['#3398DB'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line',
            lineStyle: {
              opacity: 0
            }
          },
          formatter: function (prams) {
            if (prams[0].data === 0) {
              return prams[0].name + "人数：0"
            } else {
              return prams[0].name + "人数：" + prams[0].data
            }
          }
        },
        grid: {
          left: '4%',
          right: '3%',
          bottom: '20%',
          top: '7%',
        },
        xAxis: [{
          type: 'category',
          gridIndex: 0,
          data: xData.value,
          axisTick: {
            alignWithLabel: true
          },
          axisLine: {
            lineStyle: {
              color: '#0c3b71'
            }
          },
          axisLabel: {
            interval: 0,
            rotate: 40,
            show: true,
            color: '#e2e9ff',
            fontSize: 13
          }
        }],
        yAxis: [{
          type: 'value',
          gridIndex: 0,
          splitLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          min: 0,
          max: valueMax + 5,
          axisLine: {
            lineStyle: {
              color: '#0c3b71'
            }
          },
          axisLabel: {
            color: 'rgb(170,170,170)',
            formatter: '{value}'
          }
        },
        {
          type: 'value',
          gridIndex: 0,
          min: 0,
          max: 50,
          splitNumber: 12,
          splitLine: {
            show: false
          },
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            show: false
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: ['rgba(250,250,250,0.0)', 'rgba(250,250,250,0.05)']
            }
          }
        }
        ],
        series: [{
          name: '人数',
          type: 'bar',
          barWidth: '10',
          xAxisIndex: 0,
          yAxisIndex: 0,
          itemStyle: {

            borderRadius: 30,
            color: new barEchart.graphic.LinearGradient(
              0, 0, 0, 1, [{
                offset: 0,
                color: '#00feff'
              },
              {
                offset: 0.5,
                color: '#027eff'
              },
              {
                offset: 1,
                color: '#0286ff'
              }
            ]
            )

          },

          data: yData.value,
          zlevel: 11,
          label: {
            show: true,
            lineHeight: 25,
            width: 25,
            height: 25,
            backgroundColor: 'rgba(0,160,221,0.1)',
            borderRadius: 200,
            position: ['-8', '-30'],
            distance: 1,
            formatter: [
              '{a|{c}}'
            ].join(','),
            rich: {
              d: {
                color: '#3CDDCF',
              },
              a: {
                color: '#fff',
                align: 'center',
              },
              b: {
                width: 1,
                height: 15,
                borderWidth: 1,
                borderColor: '#234e6c',
                align: 'left'
              },
            }

          }
        },

        ]
      })
      window.onresize = function () {
        //自适应大小
        myBarChart.resize();
      };
    }


    onMounted(() => {
      getDeptData()
    })


    return { isPcsBarData, changeBarData }
  }
}
</script>

<style scoped lang="scss">
.pop-content {
  font-size: 16px;
  font-weight: bold;
  line-height: 3vh;
}

.barChart {
  width: 100%;
  height: 23vh;
}

.title {
  height: 5vh;

  .font {
    color: white;
    font-size: 20px;
    float: left;
    width: 130px;
    margin: 14px 0px 0px 18px;
    text-shadow: #13b3ff 0 0 0px;
  }



  .buttonDiv {
    float: right;
    margin: 10px 15px 0px 0px;

    .el-button {
      font-size: 16px;
    }

    .el-button.is-disabled {
      color: #318ef0;
    }

    .el-button--text {
      color: #8c939d;
      font-weight: bold;
    }
  }
}
</style>