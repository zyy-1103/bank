let id = $.cookie("id");
let word=$.cookie("word")
Vue.createApp({
    data(){
        return{
            c:id,
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
        if (id != null) {
            axios({
                url:"getComInfo",
                method:"post",
                data:{
                    id:id,
                    word:word
                }
            }).then(res=>{
                if(res.data==''){
                    this.loginText = "正在准备中...";
                }else {
                    let r=res.data;
                    res=res.data.data;
                    this.capable=r.capable;
                    if (!this.capable) {
                        this.loginText = "您不符合本次活动规则";
                    }
                    this.start=res.startTime;
                    this.quantity=res.quantity;
                    this.price=res.price;
                    this.total=res.total;
                    this.endTime=res.endTime;
                }
            });
        }
    }
}).mount("#all");