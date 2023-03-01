<template>
  <div>
    
    <!--查询表单-->
    <el-card class="operate-container" shadow="never">
      <el-form :inline="true" class="demo-form-inline">
      <el-button type="primary" icon="el-icon-plus" @click="dialogVisible = true">新建</el-button>
      <el-button type="primary" icon="el-icon-delete" @click="delBatch()">删除</el-button>&nbsp;
      <el-form-item>
        <el-input v-model="searchObj.userName" placeholder="姓名" /> 
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchObj.sex" clearable placeholder="性别">
          <el-option value="男" label="男"/>
          <el-option value="女" label="女"/>
        </el-select>
      </el-form-item>
      
      <el-button type="primary" icon="el-icon-search" @click="getUserList()">查询</el-button>
      <el-button type="default" icon="el-icon-refresh" @click="resetData()">清空</el-button>
    </el-form>
    </el-card>
    
    <!-- 表格 -->
    <el-table
      :data="list"
      border
      stripe
      @selection-change="handleSelectionChange">
      <el-table-column type="selection"/>
      <el-table-column prop="userName" label="名称"  />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="sex" label="性别"  />
      <el-table-column prop="phoneNum" label="联系电话"  />
      <el-table-column prop="fullAddress" label="详细地址" />
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" icon="el-icon-edit" @click="saveForm(scope.row)">编辑</el-button>
          <el-button type="primary" size="mini" icon="el-icon-delete" @click="onRemove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 20, 50, 100]"
      style="padding: 30px 0; text-align: center;"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="changePageSize"
      @current-change="changeCurrentPage"
    />

    <!-- 添加用户的对话框 -->
    <el-dialog title="添加新用户" :visible.sync="dialogVisible" width="50%" @close="onDialogClosed">
      <!-- 添加用户的表单 -->
      <el-form :model="form" label-width="80px" :rules="formRules" ref="myaddForm">
        <!-- 采集用户的姓名 -->
        <el-form-item label="姓名" prop="userName">
          <el-input v-model="form.userName"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model.number="form.age"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="form.sex">
            <el-option value="男" label="男"/>
            <el-option value="女" label="女"/>
          </el-select>
        </el-form-item>
        
        <el-form-item label="联系电话" prop="phoneNum">
          <el-input v-model="form.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="区域" prop="preAddress">
          <el-cascader
            size="large"
            :options="options"
            v-model="form.preAddress"
            @change="handleChange"
          >
          </el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="sufAddress">
          <el-input v-model="form.sufAddress" placeholder="请输入详细地址"></el-input>
        </el-form-item>
        
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="onAddNewUser">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑用户的对话框 -->
    <el-dialog title="编辑用户" :visible.sync="editVisible" width="35%" @close="onDialogUpdateClosed">
      <el-form :model="nowForm" label-width="80px" :rules="formRules" ref="myupdateForm">
        <el-form-item label="姓名" prop="userName">
          <el-input v-model="nowForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model.number="nowForm.age"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="nowForm.sex">
            <el-option value="男" label="男"/>
            <el-option value="女" label="女"/>
          </el-select>
        </el-form-item>
        <el-form-item label="联系电话" prop="phoneNum">
          <el-input v-model="nowForm.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="区域" prop="preAddress">
          <el-cascader
            size="large"
            :options="options"
            v-model="nowForm.preAddress"
            @change="handleChange"
          >
          </el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="sufAddress">
          <el-input v-model="nowForm.sufAddress" placeholder="请输入详细地址"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateUser">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { regionData, CodeToText } from "element-china-area-data"
import axios from "axios"
export default {
  name: 'UserList',
  data() {
    let checkAge = (rule, value, cb) => {
      if (!Number.isInteger(value)) {
        return cb(new Error('请填写整数'))
      }
      if (value > 200) {
        return cb(new Error('请填写正确的年龄'))
      }
      cb()
    }
    return {
      options: regionData,
      selectedOptions: [],
      list: [], // 用户列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 10, // 每页记录数
      searchObj: {
      }, // 查询条件
      multipleSelection: [],// 批量删除选中的记录列表
      form: {
        userName: '',
        age: '',
        sex: '',
        phoneNum: '',
        preAddress: '',
        sufAddress: '',
        fullAddress: ''
      },
      nowForm: {
        id: '',
        userName: '',
        age: '',
        sex: '',
        phoneNum: '',
        preAddress: '',
        sufAddress: '',
        fullAddress: ''
      },
      dialogVisible: false,
      editVisible:false,
      formRules: {
        userName: [
          { required: true, message: '姓名是必填项', trigger: 'blur' },
          { min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur' },
        ],
        age: [
          { required: true, message: '年龄是必填项', trigger: 'blur' },
          { validator: checkAge, trigger: 'blur' },
        ],
        sex: [
          { required: true, message: '性别是必填项'},
        ],
        phoneNum: [
          { required: true, message: '联系电话是必填项', trigger: 'blur' },
          { min: 11, max: 11, message: '请输入正确的手机号', trigger: 'blur' },
        ],
        preAddress: [
          { required: true, message: '区域是必填项' },
        ],
        sufAddress: [
          { required: true, message: '地址是必填项', trigger: 'blur' },
        ]
      },
      multipleSelection: []
    }
  },
  created() {
    this.getUserList()
  },
  methods: {
    // 获取用户列表的数据
    async getUserList() {
      const { data: res } = await this.$http.post('/findPage/'+this.page+'/'+this.limit, this.searchObj)
      // axios.post('/findPage/'+this.page+'/'+this.limit, this.searchObj).then(res => {
      //   console.log('res:'+res.data)
      // })
      console.log('res:'+res.data.records)
      if (res.status !== 0) return console.log('用户列表数据获取失败！')
      this.list = res.data.records
      this.total = res.data.total
    },
    resetData() {
      this.searchObj = {}
      this.getUserList()
    },
    async delBatch() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的记录！')
        return
      }
      const confirmResult = await this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(err => err)

      if(confirmResult !== 'confirm') return this.$message.info('取消了删除！')

      var idList = []
      this.multipleSelection.forEach(item => {
        idList.push(item.id)
      })
      console.log(idList)
      const { data: res } = await this.$http.put('/delBatch', idList)
      if(res.status !== 0) return this.$message.info('删除失败！')

      this.$message.info('删除成功！')
      this.getUserList()
    },
    handleSelectionChange(selection) {
      console.log(selection)
      this.multipleSelection = selection
    },
    // 监听对话框关闭的事件
    onDialogClosed() {
      // 拿到 Form 组件的引用，调用 resetFields 函数，即可重置表单
      this.$refs.myaddForm.resetFields()
    },
    onDialogUpdateClosed() {
      this.$refs.myupdateForm.resetFields()
      this.nowForm = {
        id: '',
        userName: '',
        age: '',
        sex: '',
        phoneNum: '',
        address: ''
      }
    },
    // 用户点击了添加按钮
    onAddNewUser() {
      this.$refs.myaddForm.validate(async valid => {
        if (!valid) return
        // 需要执行添加的业务处理
        var loc = "";
        for (let i = 0; i < this.form.preAddress.length; i++) {
          loc += CodeToText[this.form.preAddress[i]];
        }
        this.form.fullAddress = loc + this.form.sufAddress;
        let l = '';
        for (let i = 0; i<this.form.preAddress.length; i++) {
          l += this.form.preAddress[i]
          l += ','
        }
        this.form.preAddress = l.substring(0, l.length - 1)
        
        const { data: res } = await this.$http.post('/addUser', this.form)
        if (res.status !== 0) return this.$message.error('添加用户失败！')
        this.$message.success('添加成功！')
        this.form = {
          userName: '',
          age: '',
          sex: '',
          phoneNum: '',
          preAddress: '',
          preAddressString: '',
          sufAddress: ''}
        this.dialogVisible = false
        this.getUserList()
      })
    },
    updateUser() {
      this.$refs.myupdateForm.validate(async valid => {
        if (!valid) return
        var loc = "";
        for (let i = 0; i < this.nowForm.preAddress.length; i++) {
          loc += CodeToText[this.nowForm.preAddress[i]];
        }
        this.nowForm.fullAddress = loc + this.nowForm.sufAddress
        let preString = ''
        this.nowForm.preAddress.forEach(item => {
          preString += item
          preString += ','
        })
        this.nowForm.preAddress = preString.substring(0, preString.length - 1)
        
        const { data: res } = await this.$http.put('/updateUser', this.nowForm)
        if (res.status !== 0) return this.$message.error('修改用户失败！')
        this.$message.success('修改成功！')
        this.editVisible = false
        this.getUserList()
      })
    },
    async saveForm(row) {
      this.editVisible = true
      this.nowForm.id = row.id
      this.nowForm.userName = row.userName
      this.nowForm.age = row.age
      this.nowForm.sex = row.sex
      this.nowForm.phoneNum = row.phoneNum
      this.nowForm.preAddress = row.preAddress.split(',')
      this.nowForm.sufAddress = row.sufAddress
    },
    // 点击了删除的链接
    async onRemove(id) {
      // 询问用户是否删除
      const confirmResult = await this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).catch(err => err)

      // 判断是否点击了确认按钮
      if (confirmResult !== 'confirm') return this.$message.info('取消了删除！')

      // 发起请求，删除指定 id 的数据
      const { data: res } = await this.$http.delete('/delUser/' + id)
      if (res.status !== 0) return this.$message.error('删除失败！')
      // 提示删除成功，并刷新列表数据
      this.$message.success('删除成功！')
      this.getUserList()
    },
    // 每页记录数改变，size：回调参数，表示当前选中的“每页条数”
    changePageSize(size) {
      this.limit = size
      this.getUserList()
    },
    // 改变页码，page：回调参数，表示当前选中的“页码”
    changeCurrentPage(page) {
      this.page = page
      this.getUserList()
    },
    handleChange() {
      var loc = "";
      for (let i = 0; i < this.selectedOptions.length; i++) {
        loc += CodeToText[this.selectedOptions[i]];
      }
      console.log(loc)
    }
  },
}
</script>

<style scoped lang="less">
.el-table {
  margin-top: 15px;
}
</style>
