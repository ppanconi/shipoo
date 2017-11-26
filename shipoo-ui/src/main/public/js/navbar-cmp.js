Vue.component('navbar', {
    template: `
<nav class="navbar" role="navigation" aria-label="main navigation">
  <div class="navbar-brand">
    <a class="navbar-item" href="/">
           Shipoo
    </a>
    
    <button class="button navbar-burger">
      <span></span>
      <span></span>
      <span></span>
    </button>
  </div>
  
  <div class="navbar-menu">
      <div class="navbar-start">
        <a class="navbar-item" href="/">
            Home
        </a>      
        <div class="navbar-item has-dropdown is-hoverable">
            <a class="navbar-link" href="">
              Current Tenant
            </a>
            <div class="navbar-dropdown is-boxed">
              <a class="navbar-item" href="#">
                Tennat 2
              </a>
              <a class="navbar-item" href="#">
                Tennat 3
              </a>
              
              <hr class="navbar-divider">
              
              <a class="navbar-item" href="#">
                Create new
              </a>
            </div>
        </div>
      </div>
    
      <div class="navbar-end">
        <div class="navbar-item has-dropdown is-hoverable">
            
            <a class="navbar-link" href="">
              {{this.profile.firstName}}
            </a>
            <div class="navbar-dropdown is-boxed">
            
                <a class="navbar-item" href="#">
                    Profile
                </a>
                <hr class="navbar-divider">
                  
                <a class="navbar-item" href="#">
                    Logout
                </a>
            </div>
        </div>
      </div>
  </div>
</nav>
    `,

    data() {
        return {
            profile: window.profile
        };
    }
})