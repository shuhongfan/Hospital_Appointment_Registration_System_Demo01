<template>
  <div class="app-container">

    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.hosname" placeholder="医院名称"/>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchObj.hoscode" placeholder="医院编号"/>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
    </el-form>

    <!-- 工具条 -->
    <div>
      <el-button type="danger" size="mini" @click="removeRows()">批量删除</el-button>
    </div>


    <!-- banner列表 -->
    <el-table :data="list" stripe style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column type="index" width="50"/>
      <el-table-column prop="hosname" label="医院名称"/>
      <el-table-column prop="hoscode" label="医院编号"/>
      <el-table-column prop="apiUrl" label="api基础路径" width="200"/>
      <el-table-column prop="contactsName" label="联系人姓名"/>
      <el-table-column prop="contactsPhone" label="联系人手机"/>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? '可用' : '不可用' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button type="danger" size="mini"
                     icon="el-icon-delete" @click="removeDataById(scope.row.id)"
          >删除
          </el-button>
          <el-button v-if="scope.row.status==1" type="primary" size="mini"
                     icon="el-icon-delete" @click="lockHostSet(scope.row.id,0)"
          >锁定
          </el-button>
          <el-button v-if="scope.row.status==0" type="danger" size="mini"
                     icon="el-icon-delete" @click="lockHostSet(scope.row.id,1)"
          >取消锁定
          </el-button>
          <router-link style="margin-left: 10px" :to="'/hospSet/edit/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit"></el-button>
          </router-link>
        </template>
      </el-table-column>

    </el-table>

    <!--    分页展示-->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="current"
      :page-sizes="[100, 200, 300, 400]"
      :page-size="limit"
      style="padding: 30px 0;text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

  </div>
</template>

<script>
import hospSet from '@/api/hospSet'

export default {
  name: 'list',
  data() {
    return {
      current: 1,
      limit: 3,
      searchObj: {
        hosname: '',
        hoscode: ''
      },
      list: [],
      total: 0,
      multipleSelection: [] // 批量选择中选择的记录列表
    }
  },
  created() {
    this.getList()
  },
  methods: { // 定义方法，进行请求接口调用
    // 医院设置列表
    getList(page = 1) {
      this.current = page
      hospSet.getHospSetList(this.current, this.limit, this.searchObj)
        .then(res => { // 请求成功response是接口返回数据
          console.log(res)
          this.list = res.data.records
          this.total = res.data.total
        })
        .catch(err => { // 请求失败
          console.log(err)
        })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.current = val
      this.getList()
    },
    removeDataById(id) {
      this.$confirm('此操作将永久删除该设置信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        hospSet.deleteHospSet(id)
          .then(res => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            // 刷新页面
            this.getList()
          })
          .catch(err => {
            this.$message({
              type: 'error',
              message: '删除失败!'
            })
          })
      })
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    removeRows() {
      this.$confirm('此操作将永久删除该设置信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = []
        let i = 0
        this.multipleSelection.forEach(select => {
          ids[i] = select.id
          i++
        })
        hospSet.batchDeleteHospSet(ids)
          .then(res => {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            // 刷新页面
            this.getList()
          })
          .catch(err => {
            this.$message({
              type: 'error',
              message: '删除失败!'
            })
          })
      })
    },
    lockHostSet(id, status) {
      hospSet.lockHospSet(id, status)
        .then(res => {
          this.$message({
            type: 'success',
            message: status == 0 ? '锁定成功!' : '解锁成功！'
          })
          // 刷新页面
          this.getList()
        })
        .catch(res => {
          this.$message({
            type: 'error',
            message: '错误!'
          })
        })

    }
  }
}
</script>

<style scoped>

</style>
