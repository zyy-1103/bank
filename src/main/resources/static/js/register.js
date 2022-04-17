Vue.createApp({
    data(){
        return{
            user:'',
            pass:'',
            pass2:'',
            email:'',
            id:'',
            code:'',
            workState:'在职',
            addr:"北京",
            age:0
        }
    },
    methods:{
        register(){
            let p=/[0-9Xx]{18}/
            let c=/[\u4e00-\u9fa5]{2,8}/
            let e = /^[A-Za-z0-9]+([_.][A-Za-z0-9]+)*@([A-Za-z0-9\-]+\.)+[A-Za-z]{2,6}$/;
            let pass=/[a-zA-Z._0-9]{6,16}/
            if (!this.user || !this.pass || !this.pass2 || !this.email || !this.id || !this.code || !this.workState||!this.addr) {
                alert("输入框不能为空");
                return
            }else if (this.pass != this.pass2) {
                alert("两次密码不一致");
                return
            }else if (!p.test(this.id)) {
                alert("请输入正确的身份证号码");
                return;
            }else if(!c.test(this.user)){
                alert("请输入正确的姓名")
                return
            }else if(!e.test(this.email)){
                alert("请输入正确的邮箱格式");
                return
            }else if(!pass.test(this.password)){
                alert("请输入正确的密码格式(6-16位数字字母下划线小数点)");
                return
            }
            this.age=new Date().getFullYear()-this.id.substring(6,10);
            axios({
                url:"register",
                method:"post",
                data:{
                    user:this.user,
                    password:this.pass,
                    email:this.email,
                    idNum:this.id,
                    workState:this.workState,
                    address:this.addr,
                    age:this.age
                }
            }).then(res=>{
                if(res.data=="1"){
                    alert("注册成功");
                    location.href = "/login.html";
                }else if (res.data == "您已注册过用户") {
                    alert(res.data);
                    location.href = "/login.html";
                } else {
                    alert(res.data);
                }
            });
        }
    }
}).mount("#all")