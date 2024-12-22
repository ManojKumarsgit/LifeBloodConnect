document.addEventListener('DOMContentLoaded', function () {
    var sidebarCollapse = document.getElementById('sidebarCollapse');
    var sidebar = document.getElementById('sidebar');

    sidebarCollapse.addEventListener('click', function () {
        sidebar.classList.toggle('active');
    });
});


const navbarToggleButton = document.querySelector('.navbar-toggler');
const navbarCollapse = document.querySelector('.navbar-collapse');

navbarToggleButton.addEventListener('click', () => {
  navbarCollapse.classList.toggle('show');
});

