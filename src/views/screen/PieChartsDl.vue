<template>
  <div>
    <div id="pieChartDl" ref="pieChartDl" class="pieChart"></div>

  </div>
</template>

<script>

import { inject, ref, onMounted, reactive } from "vue"
import { getPartyAge } from "@/api/screen"
import bus from "@/bus"
import { postRetireList } from '@/api/retiree'

export default {
  name: "PieChartsDl",

  setup() {
    const pieChartDl = ref(null)
    const pieEchart = inject('ec')

    let labelArr = []
    let dataArr = []
    let sumCount = 0
    let title = ref('')
    let tableData = reactive({
      data: []
    })

    function getPartyAgeData() {
      getPartyAge().then(res => {
        if (res.code === 200) {
          for (let item of res.data) {
            labelArr.push(item.name)
            dataArr.push(item.value)
            sumCount += item.value
          }
        }
        initChart()
      })
    }
    //获取列表
    function retireeList(ageStart, ageEnd) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        partyAgeStart: ageStart,
        partyAgeEnd: ageEnd,
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

      let myPieChart = pieEchart.init(pieChartDl.value)

      myPieChart.on('click', function (params) {

        if (params.name === '20年及以下') {
          retireeList(0, 20)
          title.value = '20年及以下'
        } else if (params.name === '20-30年') {
          retireeList(20, 30)
          title.value = '20-30年'
        } else if (params.name === '30-40年') {
          retireeList(30, 40)
          title.value = '30-40年'
        } else if (params.name === '40-50年') {
          retireeList(40, 50)
          title.value = '40-50年'
        } else if (params.name === '50年及以上') {
          retireeList(50, 200)
          title.value = '50年及以上'
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
              case labelArr[0]:
                return (
                  labelArr[0] + '{percent|\r' + (dataArr[0] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[0] + '人'
                );
              case labelArr[1]:
                return (
                  labelArr[1] + '{percent|\r' + (dataArr[1] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[1] + '人'
                );
              case labelArr[2]:
                return (
                  labelArr[2] + '{percent|\r' + (dataArr[2] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[2] + '人'
                );
              case labelArr[3]:
                return (
                  labelArr[3] + '{percent|\r' + (dataArr[3] / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[3] + '人'
                );
              case labelArr[4]:
                return (
                  labelArr[4] + '{percent|\r' + (dataArr[4] / sumCount * 100).toFixed(1) + '%} ' +
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
                name: labelArr[0],
                itemStyle: {
                  color: '#0286ff'
                },
              },
              {
                value: dataArr[1],
                name: labelArr[1],
                itemStyle: {
                  color: '#ffd302'
                },
              },
              {
                value: dataArr[2],
                name: labelArr[2],
                itemStyle: {
                  color: '#fb5274'
                },
              },
              {
                value: dataArr[3],
                name: labelArr[3],
                itemStyle: {
                  color: 'green'
                },
              },
              {
                value: dataArr[4],
                name: labelArr[4],
                itemStyle: {
                  color: 'red'
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
                name: labelArr[0],
                itemStyle: {
                  color: '#0286ff',
                  opacity: 0.4,
                },
              },
              {
                value: dataArr[1],
                name: labelArr[1],
                itemStyle: {
                  color: '#ffd302',
                  opacity: 0.4,
                },
              },
              {
                value: dataArr[2],
                name: labelArr[2],
                itemStyle: {
                  color: '#fb5274',
                  opacity: 0.4,
                },
              },
              {
                value: dataArr[3],
                name: labelArr[3],
                itemStyle: {
                  color: 'green',
                  opacity: 0.4,
                },
              },
              {
                value: dataArr[4],
                name: labelArr[4],
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
                name: labelArr[0],
                itemStyle: {
                  color: '#0286ff',
                  opacity: 0.1,
                },
              },
              {
                value: dataArr[1],
                name: labelArr[1],
                itemStyle: {
                  color: '#ffd302',
                  opacity: 0.1,
                },
              },
              {
                value: dataArr[2],
                name: labelArr[2],
                itemStyle: {
                  color: '#fb5274',
                  opacity: 0.1,
                },
              },
              {
                value: dataArr[3],
                name: labelArr[3],
                itemStyle: {
                  color: 'green',
                  opacity: 0.1,
                },
              },
              {
                value: dataArr[4],
                name: labelArr[4],
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
      getPartyAgeData()
    })

    return { pieChartDl }
  }
}
</script>

<style scoped lang="scss">
.pieChart {
  width: 100%;
  height: calc(27vh - 20px);
}
</style>