let id = $.cookie("id");
Vue.createApp({
    data(){
        return{
            start:'正在准备中...',
            end:'正在准备中...',
            quantity:0,
            price:0,
            total:0,
            loginText:'进入秒杀',
            capable:0
        }
    },
    created(){
        if(id==null){
            this.loginText = "登录后才可参与";
        }else{
            axios({

            })
        }
    }
}).mount("#msg");