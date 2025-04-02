import axios from "axios";
import { getToken } from "./cache";

const request = axios.create({
    baseURL: import.meta.env.VITE_APP_API,  // api的base_url
    timeout: 60000  // 请求超时时间
});

request.interceptors.request.use(config => {
    // 文件上传的content-type
    config.headers['Content-Type'] = 'multipart/form-data'
    // jwt的token
    config.headers['Authorization'] = getToken()
    return config;
}, error => {  //请求错误处理
    Promise.reject(error)
});

// axios.post("http://localhost:8025/assist-server/upload", formData, config).then((res) => {
//         if (res.data.success) {
//           //this.photoObj = res.data.data.objectId;
//           console.log('图片上传成功！', res.data)
//           this.pics.push(res.data.data);
//           if(this.editForm.pics!=""){
//             this.editForm.pics = this.editForm.pics+","+res.data.data
//           }else{
//             this.editForm.pics = res.data.data
//           }
//           console.log(this.editForm.pics)
//         } else {
//           this.$message.error(res.data.message);
//         }
//       });

export const fileUploader = data => {
    console.log('文件上传方法：', data)
    return request({
        url: 'upload',
        method: 'post',
        data
    })
};