const router = new VueRouter({
    routes: [
        { path: '/tenant', name: 'tenant', component: tenant}
    ]
});

let http_client = {

    data() {
        return {
            HTTP: axios.create({
                baseURL: '/uiapi',
                headers: {
                    Authorization: 'Bearer {token}'
                }
            })
        }
    }

});


const app = new Vue({
    router: router,
    el: '#app',

    data : {
      client: client
    }

})

const HTTP = axios.create({
    baseURL: '/uiapi',
    headers: {
        Authorization: 'Bearer {token}'
    }
})


