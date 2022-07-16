<template>
  <div class="app-container">
    医院设置添加
    <el-form label-width="120px">
      <el-form-item label="医院名称">
        <el-input v-model="hospitalSet.hosname"/>
      </el-form-item>
      <el-form-item label="医院编号">
        <el-input v-model="hospitalSet.hoscode"/>
      </el-form-item>
      <el-form-item label="api基础路径">
        <el-input v-model="hospitalSet.apiUrl"/>
      </el-form-item>
      <el-form-item label="联系人姓名">
        <el-input v-model="hospitalSet.contactsName"/>
      </el-form-item>
      <el-form-item label="联系人手机">
        <el-input v-model="hospitalSet.contactsPhone"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import hospSet from '@/api/hospSet'

export default {
  name: 'add',
  data() {
    return {
      hospitalSet: {},
      id: ''
    }
  },
  created() {
    var id = this.$route.params.id
    if (this.$route.params && id) {
      this.id = id
      this.getHostSet(id)
    }
  },
  methods: {
    getHostSet(id) {
      hospSet.getHostSet(id)
        .then(res => {
          this.hospitalSet = res.data
        })
    },
    save() {
      hospSet.saveHospSet(this.hospitalSet)
        .then(res => {
          this.$message({
            type: 'success',
            message: '添加成功!'
          })
          this.$router.push({ path: '/hospSet/list' })
        })
    },
    update() {
      hospSet.updateHospSet(this.hospitalSet)
        .then(res => {
          this.$message({
            type: 'success',
            message: '修改成功!'
          })
          this.$router.push({ path: '/hospSet/list' })
        })
    },
    saveOrUpdate() {
      if (this.id) {
        this.update()
      } else {
        this.save()
      }
    }
  }
}
</script>

<style scoped>

</style>
