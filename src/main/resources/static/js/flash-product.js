let id = $.cookie("id");
let word=$.cookie("word")
Vue.createApp({
    data(){
        return{
            hour:'',
            minute:'',
            second:'',
            time:1,
            a:new Date(),
            url:'#'
        }
    },
    methods:{
        go(){
            axios({
                url:"seckill/"+this.url,
                method:"post",
                data:{
                    id:id,
                    word:word
                }
            }).then(res=>{
                alert(res.data);
                if (res.data == "秒杀成功") {
                    location.href = "flash-sale.html";
                }
            })
        }
    },
    created(){
        axios({
            url:"getTime",
            method:"post"
        }).then(res=>{
            res=res.data;
            if (res== -1) {
                this.hour="正在准备中..."
            }else{
                this.time = res - new Date().getTime();
                if (this.time < 0) {
                    this.time=0;
                    this.hour = this.minute = this.second = "00"
                }else{
                    let temp=new Date();
                    temp.setTime(this.time);
                    let timer=setInterval(()=>{
                        if (this.time <= 0) {
                            this.time=0;
                            this.hour = this.minute = this.second = "00"
                            clearInterval(timer);
                            return;
                        }
                        this.time-=1000;
                    },1000)
                }
            }
        })
    },
    watch:{
        time(){
            if (this.time == 0) {
                axios({
                    url:"getUrl",
                    method:"post",
                    data:{
                        id:id,
                        word:word
                    }
                }).then(res=>{
                    this.url=res.data;
                })
            }
            this.a.setTime(this.time);
            this.hour=parseInt(this.time/1000/60/60);
            this.minute=this.a.getMinutes();
            this.second=this.a.getSeconds();
        }
    }
}).mount("#all")