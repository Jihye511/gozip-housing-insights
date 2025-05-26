import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://192.168.205.52:8080/api',
    timeout: 50000,
    withCredentials : true,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default instance;
