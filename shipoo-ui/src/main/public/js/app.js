const router = new VueRouter({
    routes: [
        { path: '/tenant', name: 'tenant', component: tenant}
    ]
});


const app = new Vue({
    router: router,
    el: '#app'
})