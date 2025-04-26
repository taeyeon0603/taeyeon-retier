<template>
    <div class="container-2-1">
        <!-- <div class="scollbox">
            <div class="tr_alarm">
                <div style="width:5vw;">走访时间</div>
                <div style="width:7vw;">走访对象</div>
                <div style="width:7vw;">走访单位</div>
                <div style="width:4vw;">走访人</div>
            </div>
            <vue3-seamless-scroll :list="state.list" class="scroll" :step="0.18" :class-option="classOption">
                <div class="item" v-for="(item, index) in state.list" :key="index">
                    <div style="width:5vw;">{{ moment(item.visitDate).format("YYYY-MM-DD") }}</div>
                    <div style="width:7vw;">{{ item.visit }}</div>
                    <div style="width:7vw;">{{ item.policeStation }}</div>
                    <div style="width:4vw;">{{ item.visitBy }}</div>
                </div>
            </vue3-seamless-scroll>
        </div>
        <div class="tm-zz"></div> -->
        <swiper class="swiper-container" loop :autoplay="{ delay: 5000 }" :speed="1500" :modules="[Autoplay]"
            :direction="'vertical'">
            <swiper-slide class="swiper-slide" v-for="(item, index) in state.list " :key="index">
                <div class="lb-container">
                    <div class="img-c">
                        <img :src="item.imgUrl" @click="handleClickDetail(item)" />
                        <div class="font-c">
                            {{ item.title }}
                        </div>
                    </div>
                </div>
            </swiper-slide>
        </swiper>
        <news-detail></news-detail>
    </div>
</template>
<script>
import { ref, onMounted, reactive, computed, provide } from 'vue'
import { getWorkNews } from '@/api/screen'
import moment from 'moment';
import SwiperCore, { Autoplay } from "swiper";
SwiperCore.use([Autoplay]);
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/swiper-bundle.css";
import newsDetail from "@/components/work/newsList/newsDetail"
import bus from "@/bus"

export default {
    name: "PcsVite",
    components: {
        Swiper, SwiperSlide, newsDetail
    },
    setup() {
        const state = reactive({
            list: []
        });

        let dialog_detail = ref(false)
        provide('dialog_detail', dialog_detail)

        const getVisitRecordData = () => {
            getWorkNews().then(res => {
                if (res.code === 200) {
                    state.list = res.data
                }
            })
        }


        const classOption = computed(() => {
            return {
                step: 0.1, // 数值越大速度滚动越快
                limitMoveNum: 8, // 开始无缝滚动的数据量 this.dataList.length
                hoverStop: true, // 是否开启鼠标悬停stop
                direction: 1, // 0向下 1向上 2向左 3向右
                openWatch: true, // 开启数据实时监控刷新dom
                singleHeight: 530, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
                singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
                waitTime: 1000, // 单步运动停止的时间(默认值1000ms)
                autoPlay: true,
            }    // 注意 ref 包装过的值在value中
        })

        onMounted(() => {
            getVisitRecordData()
        })

        function handleClickDetail(rowValue) {
            dialog_detail.value = true
            bus.$emit('handleDetail', rowValue)
        }
        return { classOption, state, moment, handleClickDetail }
    }
}

</script>


<style scoped lang="scss">
.container-2-1 {
    // background-image: url(@/assets/screen/1bg.png);

    height: calc(31vh);
    width: 100%;

    .swiper-container {
        height: 100%;
        width: 100%;

        .lb-container {

            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: row;
            justify-content: center;
            align-items: center;

            .img-c {
                width: 100%;
                height: 100%;
                display: flex;
                justify-content: center;
                align-items: center;
                position: relative;

                img {
                    height: 29vh;
                    width: 21vw;
                }

                .font-c {
                    width: calc(21vw - 10px);
                    display: flex;
                    flex-direction: row;
                    justify-content: center;
                    align-items: center;
                    color: #fff;
                    background-color: #bd00009c;
                    height: 20%;
                    position: absolute;
                    left: 2vw;
                    bottom: 1vh;
                    padding: 0px 5px 0px 5px;
                }
            }


        }


    }


    // .tm-zz {
    //     width: 100%;
    //     height: 5vh;
    //     background-image: linear-gradient(to top, #020f4f 20%, rgb(255 255 255 / 0%));
    //     position: absolute;
    //     bottom: 0px;
    //     border-radius: 10px;
    // }

    // .tr_alarm {
    //     display: flex;
    //     flex-direction: row;
    //     align-items: center;
    //     margin-top: 1.2vh;

    //     div {
    //         color: #00aeda;
    //         font-size: 0.8vw;
    //         text-align: center;
    //     }
    // }

    // .scollbox {
    //     height: 100%;
    // }



    // .scroll {
    //     height: 80%;
    //     width: 25vw;
    //     margin-top: 1.6vh;

    //     overflow: hidden;

    //     .item {
    //         // background-image: url(@/assets/screen/5listbg.png);
    //         // background-color: #01202c;

    //         display: flex;
    //         align-items: center;

    //         height: 3.2vh;

    //         div {
    //             color: #e8eaeb;
    //             text-align: center;
    //             font-size: 0.7vw;
    //         }
    //     }
    // }

}
</style>