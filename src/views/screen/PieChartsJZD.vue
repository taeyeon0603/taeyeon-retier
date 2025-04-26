<template>
  <div>
    <div id="pieChart" ref="pieJZD" class="pieChart"></div>

  </div>
</template>

<script>

import { inject, ref, onMounted, reactive } from "vue"
import { getResidenceType } from "@/api/screen"
import bus from "@/bus"
import { postRetireList } from '@/api/retiree'

export default {
  name: "PieChartsJZD",
  setup() {

    const pieJZD = ref(null)
    const pieEchart = inject('ec')
    let title = ref('')
    let tableData = reactive({
      data: []
    })

    let dataArr = []
    let sumCount = 0
    function getResidenceTypeData() {
      getResidenceType().then(res => {
        if (res.code === 200) {
          for (let item of res.data) {
            dataArr.push(item)
          }
          sumCount = dataArr[0].value + dataArr[1].value + dataArr[2].value + dataArr[3].value + dataArr[4].value
          initChart()
        }
      })
    }
    function retireeList(str) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        residenceType: str,
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
      let option = {
        backgroundColor: "",
        color: ['#0286ff', '#ffd302', '#fb5274', 'green', 'red'],
        tooltip: {
          show: false,
        },
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
              case '区内':
                return (
                  '区内{percent|\r' + (dataArr[0].value / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[0].value + '人'
                );
              case '区外市内':
                return (
                  '区外市内{percent|\r' + (dataArr[1].value / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[1].value + '人'
                );
              case '市外省内':
                return (
                  '市外省内{percent|\r' + (dataArr[2].value / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[2].value + '人'
                );
              case '省外':
                return (
                  '省外{percent|\r' + (dataArr[3].value / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[3].value + '人'
                );
              case '国外':
                return (
                  '国外{percent|\r' + (dataArr[4].value / sumCount * 100).toFixed(1) + '%} ' +
                  ' ' + dataArr[4].value + '人'
                );
              default:
                break;
            }
          },
        },
        series: [
          {
            name: '居住地分布',
            type: 'pie',
            radius: ['60%', '80%'],
            center: ['30%', '50%'],
            avoidLabelOverlap: false,

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
            labelLine: {
              show: false
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '20',
                color: '#fff',
                fontWeight: 'bold',
                formatter: params => {
                  return '{a|' + params.name + '}\n\n{b|' + params.value + '}'
                },
                rich: {
                  a: {},
                  b: {}
                }
              }
            },
            data: dataArr
          }
        ]
      };

      let myPieChart = pieEchart.init(pieJZD.value)
      myPieChart.on('click', function (params) {
        retireeList(params.name)
        title.value = params.name
      });


      var currentIndex = 0;
      var timer = 0;
      function animate() {
        timer = setInterval(function () {
          var dataLen = option.series[0].data.length;
          // 取消之前高亮的图形
          myPieChart.dispatchAction({
            type: 'downplay',
            seriesIndex: 0,
            dataIndex: currentIndex,
          });
          currentIndex = (currentIndex + 1) % dataLen;
          // 高亮当前图形
          myPieChart.dispatchAction({
            type: 'highlight',
            seriesIndex: 0,
            dataIndex: currentIndex,
          });
        }, 1500);
      }
      // animate();

      // myPieChart.on("mouseover", function (e) {
      //   clearInterval(timer);
      //   // 取消之前高亮的图形
      //   myPieChart.dispatchAction({
      //     type: 'downplay',
      //     seriesIndex: 0,
      //   });
      //   myPieChart.dispatchAction({
      //     type: 'highlight',
      //     dataIndex: e.dataIndex
      //   });
      // });

      // myPieChart.on("mouseout", function (e) {
      //   currentIndex = e.dataIndex;
      //   animate();
      // });

      myPieChart.setOption(option)
      window.onresize = function () {
        //自适应大小
        myPieChart.resize();
      };
    }


    onMounted(() => {
      getResidenceTypeData()
    })


    return { pieJZD }
  }
}
</script>

<style scoped lang="scss">
.pieChart {
  width: 100%;
  height: calc(27vh - 20px);
}
</style>