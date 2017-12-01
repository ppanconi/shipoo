const tenant = Vue.component('tenant', {
    template: `
    <div>
        <div>
            <label>Tenant:</label>  <input v-model="tenant.name"> 
        </div>
        <div>
            <button v-on:click="create()">Submit</button> 
        </div>
    </div>
    `,

    data() {
        return {
            tenant: {name: ''}
        }
    },

    methods: {
        create() {
            debugger;
            this.$root.client.HTTP.post('/tenant', this.tenant
            ).then(response => {
                alert(response)
            }).catch(e => {
                alert("Error " + e);
            })
        }
    }
})
