Vue.createApp({
    data() {
        return {
            email: '',
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
                    email:this.email,
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