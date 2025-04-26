<template>
  <div>
    <div class="title">


      <el-tooltip effect="customized" placement="top-start">
        <template #content>
          <p class="pop-content">
            所在辖区地：为退休人员长期所在的辖区地。
          </p>

        </template>
        <div class="font">所在辖区地</div>
      </el-tooltip>
    </div>
    <div id="barChartRight" class="barChart"></div>
  </div>
</template>

<script>

import { inject, ref, onMounted, reactive } from "vue"
import { getDeptDist } from "@/api/screen"
import bus from "@/bus"
import { postRetireList } from '@/api/retiree'
import DialogTable from "@/views/screen/DialogTable"


export default {
  name: "BarChartsRight",
  components: {
    DialogTable,
  },
  setup() {

    const barEchartRight = inject('ec')
    let xData = []
    let yData = []
    let title = ref('')
    let tableData = reactive({
      data: []
    })

    function sortId(a, b) {
      return b.value - a.value
    }
    function retireeList(dept) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        residencePoliceStation: dept,
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

    function getDeptData() {
      getDeptDist().then(res => {
        if (res.code === 200) {
          let array = res.data.residentialPoliceStation
          array.sort(sortId)

          for (let i = 0; i <= array.length - 1; i++) {
            xData.push(array[i].name)
            yData.push(array[i].value)
          }

          initChart(array[0].value)
        }
      })
    }

    function initChart(valueMax) {
      barEchartRight.init(document.getElementById('barChartRight')).dispose()
      let myBarChartRight = barEchartRight.init(document.getElementById('barChartRight'))
      myBarChartRight.on('click', function (params) {
        retireeList(params.name)
        title.value = params.name
      });
      myBarChartRight.setOption({
        backgroundColor: '',
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: '5%',
          right: '3%',
          left: '5%',
          bottom: '20%'
        },
        xAxis: [{
          type: 'category',
          data: xData,
          axisLine: {
            lineStyle: {
              color: 'rgba(255,255,255,0.12)'
            }
          },
          axisLabel: {
            interval: 0,
            margin: 10,
            color: '#e2e9ff',
            fontSize: 13

          },
        }],
        yAxis: [{
          min: 0,
          max: valueMax + 10,
          axisLabel: {
            formatter: '{value}',
            color: '#e2e9ff',
          },
          axisLine: {
            show: false
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255,255,255,0.12)'
            }
          }
        }],
        series: [{
          type: 'bar',
          data: yData,
          barWidth: '10px',
          itemStyle: {

            color: new barEchartRight.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: 'rgba(0,244,255,1)' // 0% 处的颜色
            }, {
              offset: 1,
              color: 'rgba(0,77,167,1)' // 100% 处的颜色
            }], false),
            borderRadius: [30, 30, 30, 30],
            shadowColor: 'rgba(0,160,221,1)',
            shadowBlur: 4,

          },
          label: {
            show: true,
            lineHeight: 25,
            width: 35,
            height: 25,
            backgroundColor: 'rgba(0,160,221,0.1)',
            borderRadius: 200,
            position: ['-13', '-45'],
            distance: 1,
            formatter: [
              ' {d|●}',
              '  {a|{c}}     \n',
              '    {b|}'
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
        }]
      })
      window.onresize = function () {
        //自适应大小
        myBarChartRight.resize();
      };
    }


    onMounted(() => {

      getDeptData()
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
    width: 250px;
    margin: 14px 0px 0px 18px;
    text-shadow: #13b3ff 0 0 0px;
  }

}
</style>