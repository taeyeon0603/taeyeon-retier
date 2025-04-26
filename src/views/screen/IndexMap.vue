<template>
  <div>
    <div id="map" class="map"></div>

  </div>
</template>

<script>
import { getMapDataJson } from '@/api/getMapDataJson'
import { ref, reactive, onMounted, inject, provide } from 'vue'
import { getDeptDist } from "@/api/screen"
import { postRetireList } from '@/api/retiree'
import 'aos/dist/aos.css'
import AOS from 'aos/dist/aos.js'
import { useRouter } from 'vue-router'
import moment from 'moment'

import bus from "@/bus"

// import {getColorData} from '@/api/getMapDataJson'
// import {getYearSelectItem} from '@/api/getMapDataJson'

// import {getCommunityScore} from "@/api/index"
// import {getPcsScore} from "@/api/index"
// import {getYearItem} from "@/api/index"


export default {
  name: "IndexMap",

  setup() {
    let router = useRouter()
    let myMap = ref(null)
    const barEchart = inject('ec')
    let isInit = ref(0)
    let title = ref('')
    let tableData = reactive({
      data: []
    })
    let retireeDetailVisible = ref(false)
    provide('retireeDetailVisible', retireeDetailVisible)

    const handleClick = () => {
      router.replace('/screen')
    }

    function getDeptData() {
      getDeptDist().then(res => {
        if (res.code === 200) {
          getMapsData(res.data.residentialPoliceStation)
        }
      })
    }

    //获取列表
    function retireeList(dept) {
      postRetireList({
        page: 1,
        pageSize: 1000,
        residencePoliceStation: dept,
        isHistory: false,
      }).then(res => {
        if (res.code === 200) {
          tableData.data = res.data.content
          bus.$emit('retireeListDialog', {
            list: tableData.data,
            title: title.value
          })
        }
      }).catch(err => {
      })
    }

    function getMapsData(val) {
      // console.log('---------')
      getMapDataJson("")
        .then(res => {
          if (isInit.value == 0) {
            myMap.value = barEchart.init(document.getElementById('map'), null, { renderer: 'svg' });
            barEchart.registerMap('lcga', res);
            isInit.value = 1
            // console.log(myMap)
            myMap.value.on('click', function (params) {
              retireeList(params.name)
              // dialogVisible.value = true
            });
          }
          let option = {
            tooltip: {
              trigger: 'item',
              formatter: '{b}人数：{c}'
            },

            visualMap: {
              type: 'piecewise',
              precision: 0,
              show: true,
              pieces: [
                { min: 30, color: '#0d47a1' },
                { min: 21, max: 30, color: '#0277bd' },
                { min: 10, max: 20, color: '#2196f3' },
                { lte: 9, color: '#81d4fa' },
              ],
              textStyle: {
                color: "#fff",
              }
            },
            series: [{
              name: '派出所辖区地图',
              type: 'map',
              roam: true,
              zoom: 1.2,
              data: val,
              map: 'lcga',
              label: {
                show: true,
                color: '#ffffff',
                fontSize: '14',
                fontWeight: 'bold',
                formatter: function (params) {
                  let labelName = ''
                  if (!isNaN(params.value)) {
                    if (params.name == '中山') {
                      labelName = '{a|' + params.name + '\n' + params.value + '}'
                    } else {
                      labelName = params.name + '\n' + params.value
                    }

                  } else {
                    labelName = params.name + '\n' + '0'
                  }
                  return labelName
                },
                rich: {
                  a: {
                    color: '#f1ff0a',
                    fontSize: '14',
                    fontWeight: 'bold',

                  }
                }


              },
              itemStyle: {

                areaColor: '#233f53',
                borderColor: '#51191f',
                borderWidth: 1.2,
                emphasis: {
                  areaColor: '#00aeee'
                }
              }
            }]
          };
          myMap.value.setOption(option);
          const that = this
          let htmlStr = ''
        })
        .catch(err => console.log(err))
    }
    function formatRetireDate(val) {
      if (val.retireDate) {
        return moment(val.retireDate).format("YYYY-MM-DD")
      } else {
        return ""
      }
    }

    onMounted(() => {
      getDeptData()
      AOS.init({
        offset: 200,
        duration: 1000,
        easing: 'ease-in-out-quad',
        delay: 100
      })
    })


    return { handleClick, title, formatRetireDate, tableData }

  }


  // props: {
  //   handleClose: {
  //     type: Function
  //   }
  // },
  //   data() {
  //   return {
  //     dialogVisible: false,
  //     tableData: [],
  //     dialogdetailVisible: false,
  //     url: require('../../assets/image/qxda-jzc.jpg'),
  //     colorData: [],
  //     selectData: [],
  //     selectValue: "",
  //     isInit: 0,
  //     pcsTitle: ""
  //   }
  // },
  // mounted() {

  // },
  // created() {
  // this.getYearSelectItem()
  // this.getMapColorDatas()
  // this.getYearSelectItem()
  // },
  // beforeCreate() {

  // },
  // methods: {
  // dateSelectChange(value) {
  //   this.getMapColorDatas(value)
  //   this.$emit('month', value)
  // },
  // tagClass(score, isRed) {
  //   if (isRed === true) {
  //     return 'danger'
  //   } else {
  //     if (score >= 91) {
  //       return 'success';
  //     } else if (score < 91 && score >= 86) {
  //       return '';
  //     } else if (score < 86 && score >= 80) {
  //       return 'warning';
  //     } else if (score <= 79) {
  //       return 'danger';
  //     }
  //   }
  //   return '';
  // },
  // showDetail() {
  //   this.dialogdetailVisible = true
  // },
  // getMapColorDatas(months) {
  //   getPcsScore({month: months}).then(res => {
  //     this.colorData = res.data
  //     this.getMapsData(this.colorData)

  //   }).catch(err => {

  //   })
  // },

  // getSqDatas(pcs, months) {
  //   getCommunityScore({
  //     month: months,
  //     pcs: pcs
  //   }).then(res => {
  //     this.tableData = res.data
  //   }).catch(err => {
  //   })
  // },

  // getYearSelectItem() {
  //   getYearItem().then(res => {
  //     this.selectData = res.data
  //     this.selectValue = this.selectData[0].value
  //     this.getMapColorDatas(this.selectValue)
  //     this.$emit('month', this.selectValue)
  //   }).catch(err => {

  //   })

  // },

}
</script>


<style scoped lang="scss">
.map {

  width: 48vw;
  height: 55vh;

}
</style>
