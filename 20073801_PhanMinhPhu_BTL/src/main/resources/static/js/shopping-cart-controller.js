const app = angular.module("shopping-cart-app", []);

app.controller("shopping-cart-ctrl", function($scope, $rootScope, $http) {
	 var cartCtrl = this;
	$scope.cart = {
        items: [],
         add(id) {
            var item = this.items.find(item => item.id == id);
            if (item) {
                item.qty++;
                this.saveToLocalStorage();
            }
            else {
                $http.get(`/product/${id}`).then(resp => {
                    resp.data.qty = 1;
                    this.items.push(resp.data);
                    this.saveToLocalStorage();
                })
            }
        },

        remove: function(id) {
            var index = this.items.findIndex(item => item.id == id);
            if (index !== -1) {
                this.items.splice(index, 1);
                this.saveToLocalStorage();
            }
        },

        clear: function() {
            this.items = [];
            this.saveToLocalStorage();
        },

        amt_of: function(item) {
            return item.qty * item.productPrice;
        },

        get count() {
            return this.items.map(item => item.qty).reduce((total, qty) => total + qty, 0);
        },

        get amount() {
            return this.items
                .map(item => item.qty * item.productPrice)
                .reduce((total, qty) => total + qty, 0);
        },

        saveToLocalStorage: function() {
            var json = JSON.stringify(angular.copy(this.items));
            localStorage.setItem("cart", json);
        },

        loadFromLocalStorage: function() {
            var json = localStorage.getItem("cart");
            this.items = json ? JSON.parse(json) : [];
        },
        
       updateCartItem: function(item) {
            item.qty = parseInt(item.qty); // Đảm bảo qty là một số nguyên
            item.total = item.qty * item.productPrice;
            $scope.cart.saveToLocalStorage();
        }

    };
    $scope.cart.loadFromLocalStorage();
});