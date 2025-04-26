<template>
  <div>
    <div id="pieChartAge" ref="pieChartAge" class="pieChart"></div>

  </div>
</template>

<script>

import { inject, ref, onMounted, reactive } from "vue"
import { getAgeDist } from "@/api/screen"
import bus from "@/bus"
import { postRetireList } from '@/api/retiree'

export default {
  name: "PieChartsAge",

  setup() {
    const pieChartAge = ref(null)
    const pieEchart = inject('ec')

    let dataArr = []
    let sumCount = 0
    let title = ref('')
    let tableData = reactive({
      data: []
    })

    function getAgeDistData() {
      getAgeDist().then(res => {
        if (res.code === 200) {
          dataArr.push(res.data.a50to59)
          dataArr.push(res.data.a60to69)
          dataArr.push(res.data.a70to79)
          dataArr.push(res.data.a80to89)
          dataArr.push(res.data.a90to99 + res.data.over100)

          sumCount = res.data.a50to59 + res.data.a60to69 + res.data.a70to79 + res.data.a80to89 + res.data.a90to99 + res.data.over100
        }
        initChart()
      })
    }
    //获取列表
    function retireeList(ageStart, ageEnd) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        ageStart: ageStart,
        ageEnd: ageEnd,
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


    function initChart(party, normal) {

      let myPieChart = pieEchart.init(pieChartAge.value)

      myPieChart.on('click', function (params) {
    
        if (params.name === '50-59') {
          retireeList(50, 59)
          title.value = '50-59岁'
        } else if (params.name === '60-69') {
          retireeList(60, 69)
          title.value = '60-69岁'
        } else if (params.name === '70-79') {
          retireeList(70, 79)
          title.value = '70-79岁'
        } else if (params.name === '80-89') {
          retireeList(80, 89)
          title.value = '80-89岁'
        } else if (params.name === '90以上') {
          retireeList(90, 200)
          title.value = '90岁以上'
        }

      });
      myPieChart.setOption({

        backgroundColor: '',
        legend: {
          orient: 'vertical',
          show: true,
          right: '10%',
          y: 'center',
          itemWidth: 3,
          itemHeight: 20,
          itemGap: 20,
          textStyle: {
            color: '#7a8c9f',
            fontSize: 14,
            lineHeight: 20,
            rich: {
              percent: {
                color: '#fff',
                fontSize: 16,
              },
            },
          },
          formatter: name => {
            switch (name) {
              case '50-59':
                return (
                  '50-59岁{percent|\r' + (dataArr[0] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[0] + '人'
                );
              case '60-69':
                return (
                  '60-69岁{percent|\r' + (dataArr[1] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[1] + '人'
                );
              case '70-79':
                return (
                  '70-79岁{percent|\r' + (dataArr[2] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[2] + '人'
                );
              case '80-89':
                return (
                  '80-89岁{percent|\r' + (dataArr[3] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[3] + '人'
                );
              case '90以上':
                return (
                  '90岁以上{percent|\r' + (dataArr[4] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[4] + '人'
                );
              default:
                break;
            }
          },
        },
        tooltip: {
          show: true,
          formatter: '{b}       {c}人',
        },
        series: [
          {
            type: 'pie',
            radius: ['65%', '75%'],
            center: ['30%', '50%'],
            emphasis: { scale: false },
            z: 10,
            label: {
              show: true,
              position: 'center',
              formatter: () => {
                return '总人数\r\n{total|' + sumCount + '} 人';
              },
              rich: {
                total: {
                  fontSize: 30,
                  color: '#fff',
                },
              },
              color: '#7a8c9f',
              fontSize: 16,
              lineHeight: 30,
            },
            data: [
              {
                value: dataArr[0],
                name: '50-59',
                itemStyle: {
                  color: '#0286ff',
                },
              },
              {
                value: dataArr[1],
                name: '60-69',
                itemStyle: {
                  color: '#ffd302',
                },
              },
              {
                value: dataArr[2],
                name: '70-79',
                itemStyle: {
                  color: '#fb5274',
                },
              },
              {
                value: dataArr[3],
                name: '80-89',
                itemStyle: {
                  color: 'green',
                },
              },
              {
                value: dataArr[4],
                name: '90以上',
                itemStyle: {
                  color: 'red',
                },
              },

            ],
            labelLine: {
              show: false,
            },
          },
          {
            type: 'pie',
            radius: ['54%', '64%'],
            center: ['30%', '50%'],
            emphasis: { scale: false },
            label: {
              show: false,
            },
            data: [
              {
                value: dataArr[0],
                name: '50-59',
                itemStyle: {
                  color: '#0286ff',
                  opacity: 0.4,
                },
              },
              {
                value: dataArr[1],
                name: '60-69',
                itemStyle: {
                  color: '#ffd302',
                  opacity: 0.4,
                },
              },
              {
                value: dataArr[2],
                name: '70-79',
                itemStyle: {
                  color: '#fb5274',
                  opacity: 0.4,
                },
              },
              {
                value: dataArr[3],
                name: '80-89',
                itemStyle: {
                  color: 'green',
                  opacity: 0.4,
                },
              },
              {
                value: dataArr[4],
                name: '90以上',
                itemStyle: {
                  color: 'red',
                  opacity: 0.4,
                },
              },

            ],
            labelLine: {
              show: false,
            },
          },
          {
            type: 'pie',
            radius: ['43%', '53%'],
            center: ['30%', '50%'],
            emphasis: { scale: false },
            label: {
              show: false,
            },
            data: [
              {
                value: dataArr[0],
                name: '50-59',
                itemStyle: {
                  color: '#0286ff',
                  opacity: 0.1,
                },
              },
              {
                value: dataArr[1],
                name: '60-69',
                itemStyle: {
                  color: '#ffd302',
                  opacity: 0.1,
                },
              },
              {
                value: dataArr[2],
                name: '70-79',
                itemStyle: {
                  color: '#fb5274',
                  opacity: 0.1,
                },
              },
              {
                value: dataArr[3],
                name: '80-89',
                itemStyle: {
                  color: 'green',
                  opacity: 0.1,
                },
              },
              {
                value: dataArr[4],
                name: '90以上',
                itemStyle: {
                  color: 'red',
                  opacity: 0.1,
                },
              },

            ],
            labelLine: {
              show: false,
            },
          },
        ],

      })
      window.onresize = function () {
        //自适应大小
        myPieChart.resize();
      };
    }

    onMounted(() => {
      getAgeDistData()
    })

    return { pieChartAge }
  }
}
</script>

<style scoped lang="scss">
.pieChart {
  width: 100%;
  height: calc(27vh - 20px);
}
</style>