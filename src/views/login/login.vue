<template>
  <div class="container">

    <div class="container-login">
      <div class="title">退休人员信息管理系统</div>
      <div class="form">
        <el-form :rules="rules" ref="myForm" :model="ruleForm" @keyup.enter.native="submitForm">
          <el-form-item label="" prop="username">
            <el-input prefix-icon="el-icon-user" v-model="ruleForm.username" placeholder="请输入账号"></el-input>
          </el-form-item>

          <el-form-item label="" prop="password">
            <el-input :type="passwordType" v-model="ruleForm.password" placeholder="请输入密码">
              <template #prefix>
                <i :class="{ 'el-input__icon': true, 'el-icon-lock': !cIcon, 'el-icon-unlock': cIcon }"
                  style="cursor: pointer" @click="passwordShow"></i>
              </template>

            </el-input>

          </el-form-item>

          <el-form-item>
            <el-button type="primary" :loading="submitLoading" style="width: 100%;" @click="submitForm">{{ submitState
            }}
            </el-button>
          </el-form-item>
        </el-form>

      </div>
    </div>
    <div class="footer">Copyright©2025</div>
  </div>
</template>

<script>
import { ref, reactive, provide } from 'vue'
import { login } from '@/api/login'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

export default {
  name: "login",
  setup() {

    let passwordType = ref('password')
    let passwordIsShow = ref(false)
    let cIcon = ref(false)
    let submitState = ref('登陆')
    let submitLoading = ref(false)
    let myForm = ref(null)
    let router = useRouter()
    let store = useStore()



    function passwordShow() {
      passwordIsShow.value = !passwordIsShow.value

      console.log(passwordIsShow.value)

      if (passwordIsShow.value === false) {
        passwordType.value = 'password'
        cIcon.value = false
      } else {
        passwordType.value = ''
        cIcon.value = true
      }
    }

    let ruleForm = reactive({
      username: '',
      password: ''
    })

    let rules = reactive({
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ],
    })


    function submitForm() {
      myForm.value.validate((valid) => {
        if (valid) {
          //submitLoading.value = true
          //submitState.value = '登陆中...'
          store.dispatch('user/Login', ruleForm).then(res => {
            if (res.code === 200) {
              ElMessage.success('登陆成功！')
              let username = store.state.user.userInfo.name
              if (username == "演示" || username.indexOf("派出所") != -1) {

                router.replace('/stepOne')

              } else {
                router.replace('/retiree/list')
              }
            } else {
              ElMessage.error(res.message)
              setTimeout(() => {
                submitLoading.value = false
                submitState.value = '登陆'
              }, 1000)
            }
          }).catch(err => {
            console.log(err)

          })

          // login({
          //   username: ruleForm.account,
          //   password: ruleForm.password
          // }).then(res => {
          //   if (res.code === 200) {
          //     ElMessage.success('登陆成功！')
          //     router.replace('/index')
          //   } else {
          //     ElMessage.error(res.message)
          //     setTimeout(() => {
          //       submitLoading.value = false
          //       submitState.value = '登陆'
          //     }, 1000)
          //   }
          // }).catch(err => {
          //   console.log(err)
          // })

        } else {
          // console.log('error submit')
          return false
        }
      })
    }

    return { ruleForm, rules, myForm, submitForm, passwordType, passwordShow, cIcon, submitState, submitLoading }
  }
}
</script>

<style scoped lang="scss">
.container {
  height: 100%;
  width: 100%;
  background-color: #f0f2f5;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-image: url(../../assets/images/bg-image.jpg);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .container-login {
    background-color: white;
    height: 350px;
    width: 510px;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-shadow: 0px 0px 10px #dadada;

    .title {
      margin-top: 50px;
      font-size: 34px;
      color: #404040;
      letter-spacing: 3px;
      font-family: hysx;
    }

    .form {
      margin-top: 38px;
      width: 360px;
      height: 190px;
    }
  }

  .footer {
    position: absolute;
    bottom: 3%;
    font-size: 14px;
    color: #afafaf;
  }

  :deep(.el-form-item__content) {
    display: flex;
    justify-content: center;
  }

}
</style>
