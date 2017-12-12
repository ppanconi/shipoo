const router = new VueRouter({
    routes: [
        { path: '/tenant', name: 'tenant', component: tenant}
    ]
});

let http_client = {
    data : {
        HTTP: axios.create({
            baseURL: '/uiapi',
            headers: {
                Authorization: 'Bearer {token}'
            }
        })
    }
};


const app = new Vue({
    router: router,
    el: '#app',

    mixins: [http_client],
});


