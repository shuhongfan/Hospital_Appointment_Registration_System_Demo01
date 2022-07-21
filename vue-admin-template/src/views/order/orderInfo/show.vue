<template>
  <div class="app-container">
    <h4>订单信息</h4>
    <table class="table table-striped table-condenseda table-bordered" width="100%">
      <tbody>
      <tr>
        <th width="15%">订单交易号</th>
        <td width="35%"><b style="font-size: 14px">{{ orderInfo.outTradeNo }}</b></td>
        <th width="15%">医院名称</th>
        <td width="35%">{{ orderInfo.hosname }}</td>
      </tr>
      <tr>
        <th>科室名称</th>
        <td>{{ orderInfo.depname }}</td>
        <th>医生职称</th>
        <td>{{ orderInfo.title }}</td>
      </tr>
      <tr>
        <th>安排日期</th>
        <td>{{ orderInfo.reserveDate }} {{ orderInfo.reserveTime === 0 ? '上午' : '下午' }}</td>
        <th>预约号序</th>
        <td>{{ orderInfo.number }}</td>
      </tr>
      <tr>
        <th>医事服务费</th>
        <td>{{ orderInfo.amount }}</td>
        <th>建议取号时间</th>
        <td>{{ orderInfo.fetchTime }}</td>
      </tr>
      <tr>
        <th>取号地点</th>
        <td>{{ orderInfo.fetchAddress }}</td>
        <th>退号时间</th>
        <td>{{ orderInfo.quitTime }}</td>
      </tr>
      <tr>
        <th>订单状态</th>
        <td>{{ orderInfo.param.orderStatusString }}</td>
        <th>预约时间</th>
        <td>{{ orderInfo.createTime }}</td>
      </tr>
      </tbody>
    </table>
    <h4>就诊人信息</h4>
    <table class="table table-striped table-condenseda table-bordered" width="100%">
      <tbody>
      <tr>
        <th width="15%">姓名</th>
        <td width="35%">{{ patient.name }}天</td>
        <th width="15%">证件类型</th>
        <td width="35%">{{ patient.param.certificatesTypeString }}</td>
      </tr>
      <tr>
        <th>证件编号</th>
        <td>{{ patient.certificatesNo }}</td>
        <th>性别</th>
        <td>{{ patient.sex === 1 ? '男' : '女' }}</td>
      </tr>
      <tr>
        <th>出生年月</th>
        <td>{{ patient.birthdate }}</td>
        <th>手机</th>
        <td>{{ patient.phone }}</td>
      </tr>
      <tr>
        <th>是否结婚</th>
        <td>{{ patient.isMarry === 1 ? '是' : '否' }}</td>
        <th>地址</th>
        <td>{{ patient.param.fullAddress }}</td>
      </tr>
      <tr>
        <th>联系人姓名</th>
        <td>{{ patient.contactsName }}</td>
        <th>联系人证件类型</th>
        <td>{{ patient.param.contactsCertificatesTypeString }}</td>
      </tr>
      <tr>
        <th>联系人证件号</th>
        <td>{{ orderInfo.contactsCertificatesNo }}</td>
        <th>联系人手机</th>
        <td>{{ orderInfo.contactsPhone }}</td>
      </tr>
      <br>
      <el-row>
        <el-button @click="back">返回</el-button>
      </el-row>
      </tbody>
    </table>
  </div>
</template>
<script>
// 引入组件
import orderInfoApi from '@/api/orderInfo.js'

export default {
  data() {
    return {
      orderInfo: null,
      patient: null
    }
  },

  // 生命周期方法（在路由切换，组件不变的情况下不会被调用）
  created() {
    console.log('form created ......')
    this.init()
  },
  methods: {
    // 表单初始化
    init() {
      const id = this.$route.params.id
      this.fetchDataById(id)
    },
    // 根据id查询记录
    fetchDataById(id) {
      orderInfoApi.getById(id).then(response => {
        this.orderInfo = response.data.orderInfo
        this.patient = response.data.patient
      })
    },
    back() {
      this.$router.push({ path: '/order/orderInfo/list' })
    }
  }
}
</script>
