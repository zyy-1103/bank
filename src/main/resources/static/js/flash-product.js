Vue.createApp({
    data(){
        return{
            hour:'',
            minute:'',
            second:'',
            time:0,
            a:new Date()
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
                this.time=res-new Date().getTime();
                let temp=new Date();
                temp.setTime(this.time);
                let timer=setInterval(()=>{
                    if (this.time <= 0) {
                        this.hour = this.minute = this.second = "00"
                        clearInterval(timer);
                        return;
                    }
                    this.time-=1000;
                },1000)
            }
        })
    },
    watch:{
        time(){
            this.a.setTime(this.time);
            this.hour=this.a.getHours();
            this.minute=this.a.getMinutes();
            this.second=this.a.getSeconds();
        }
    }
}).mount("#all")