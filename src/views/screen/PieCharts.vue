<template>
  <div>
    <div id="pieChart" ref="pieChart" class="pieChart"></div>
    <DialogRowBar></DialogRowBar>
  </div>
</template>

<script>

import { inject, ref, onMounted, reactive } from "vue"
import { getPartyCount } from "@/api/screen"
import bus from "@/bus"
import { postRetireList } from '@/api/retiree'
import DialogTable from "@/views/screen/DialogTable"
import DialogRowBar from "@/views/screen/DialogRowBar"

export default {
  name: "PieCharts",
  components: {
    DialogTable,
    DialogRowBar
  },
  setup() {

    const pieChart = ref(null)

    const pieEchart = inject('ec')
    let title = ref('')
    let tableData = reactive({
      data: []
    })

    function getPartyCountData() {
      getPartyCount().then(res => {
        if (res.code === 200) {
          initChart(res.data.party, res.data.normal, res.data.outerBranch)
        }
      })
    }
    function retireeList(str) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        politicalIdentity: str,
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

    function initChart(party, normal, outerBranch) {
      let myPieChart = pieEchart.init(pieChart.value)
      myPieChart.on('click', function (params) {
        console.log(params)
        if (params.name == '党员') {
          bus.$emit('dialogRowBar')

        } else if (params.name == '外支部' || params.name == '群众') {
          retireeList(params.name)
          title.value = params.name
        }
      });

      myPieChart.setOption({
        backgroundColor: '',
        color: ['#00ffff', '#ff5b00', '#0286ff'],
        graphic: {
          elements: [{
            type: "image",
            z: 3,
            style: {
              image: require('../../assets/screen/pieImg.png'),
              width: 128,
              height: 128
            },
            left: 'center',
            top: 'center',
            position: [100, 100]
          }]
        },
        tooltip: {
          show: false
        },

        toolbox: {
          show: false
        },
        series: [{
          name: '',
          type: 'pie',
          clockwise: false,
          radius: [76, 80],
          z: 5,
          emphasis: { scale: false },

          label: {
            show: true,
            color: '#fff',
            formatter: '{b} {c}' + '人',
            fontSize: 16,
          },
          itemStyle: {
            label: {
              show: true,
              position: 'outside',
              color: '#ddd',
              formatter: function (params) {
                if (params.name !== '') {
                  return params.name + '：' + params.value;
                } else {
                  return '';
                }
              },
            },
            labelLine: {
              length: 20,
              show: true,
              color: '#00ffff'
            }

          },
          data: [{
            value: party,
            name: '党员',
            itemStyle: {

              borderWidth: 5,
              shadowBlur: 20,
              borderColor: '#00ffff',
              shadowColor: '#00ffff'

            }
          }, {
            value: normal,
            name: '群众',
            itemStyle: {

              borderWidth: 5,
              shadowBlur: 20,
              borderColor: '#ff5b00',
              shadowColor: '#ff5b00'

            }
          }, {
            value: outerBranch,
            name: '外支部',
            itemStyle: {
              borderWidth: 5,
              shadowBlur: 20,
              borderColor: '#0286ff',
              shadowColor: '#0286ff'

            }
          }]
        }]
      })
      window.onresize = function () {
        //自适应大小
        myPieChart.resize();
      };
    }


    onMounted(() => {
      getPartyCountData()
    })


    return { pieChart }
  }
}
</script>

<style scoped lang="scss">
.pieChart {
  width: 100%;
  height: calc(27vh - 20px);
}
</style>