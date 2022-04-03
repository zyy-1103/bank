Vue.createApp({
    data() {
        return {
            user: '',
            password: '',
            errorMsg:''
        }
    },
    methods:{
        login(){
            axios({
                url:"/login",
                method:"post",
                data:{
                    user:this.user,
                    password:this.password
                }
            }).then(res=>{
                res=res.data;
                if (res.data == "1") {
                    location.href="/"
                }else{
                    this.errorMsg=res.data;
                }
            })
        }
    }
}).mount("#all");