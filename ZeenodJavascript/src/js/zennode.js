class Product {
    constructor(name, price) {
        this.name = name;
        this.price = price;
    }
   
   
    getPrice() {
        return this.price;
    }
   
   
    getName() {
        return this.name;
    }
   }
   
   
   class ShoppingCart {
    constructor(products) {
        this.products = products;
        this.quantities = {};
        this.giftWrapping = {};
        this.discounts = {};
    }
   
   
    inputQuantities() {
        const scanner = require('readline-sync');
        for (const product of Object.values(this.products)) {
            const quantity = parseInt(scanner.question(`Enter quantity for ${product.getName()}: `));
            this.quantities[product.getName()] = quantity;
        }
    }
   
   
    inputGiftWrapping() {
        const scanner = require('readline-sync');
        for (const product of Object.values(this.products)) {
            const wrap = scanner.question(`Is ${product.getName()} wrapped as a gift? (yes/no): `).toLowerCase();
            this.giftWrapping[product.getName()] = wrap === "yes";
        }
    }
   
   
    calculateSubtotal() {
        return Object.entries(this.products).reduce((total, [productName, product]) => {
            return total + product.getPrice() * this.quantities[productName];
        }, 0);
    }
   
   
    inputDiscount() {
        const totalQuantity = Object.values(this.quantities).reduce((sum, quantity) => sum + quantity, 0);
        const cartTotal = Object.entries(this.products).reduce((total, [productName, product]) => {
            return total + product.getPrice() * this.quantities[productName];
        }, 0);
   
   
        if (cartTotal > 200) {
            this.discounts["flat_10_discount"] = 10.0;
        }
   
   
        let maxFor10 = 0;
        for (const [productName, product] of Object.entries(this.products)) {
            const quantity = this.quantities[productName];
   
   
            if (quantity > 10) {
                const discountAmount = product.getPrice() * quantity * 0.05;
                maxFor10 = Math.max(discountAmount, maxFor10);
            }
        }
        this.discounts["bulk_5_discount"] = maxFor10;
   
   
        if (totalQuantity > 20) {
            const discountAmount = cartTotal * 0.1;
            this.discounts["bulk_10_discount"] = discountAmount;
        }
   
   
        if (totalQuantity > 30) {
            let maxFor50 = 0;
            for (const [productName, product] of Object.entries(this.products)) {
                const quantity = this.quantities[productName];
                if (quantity > 15) {
                    const discountAmount = product.getPrice() * (quantity - 15) * 0.5;
                    maxFor50 = Math.max(discountAmount, maxFor50);
                }
            }
            this.discounts["tiered_50_discount"] = maxFor50;
        }
    }
   
   
    calculateShippingFee() {
        const totalQuantity = Object.values(this.quantities).reduce((sum, quantity) => sum + quantity, 0);
        const itemsPerPackage = 10;
        const packages = Math.ceil(totalQuantity / itemsPerPackage);
        const shippingFeePerPackage = 5.0;
        return packages * shippingFeePerPackage;
    }
   
   
    calculateGiftWrapFee() {
       let totalQuantity = 0;
   
      for (const [giftKey, isGift] of Object.entries(this.giftWrapping))   {
       if (isGift) {
           if (this.quantities.hasOwnProperty(giftKey)) {
               totalQuantity += this.quantities[giftKey];
           }
       }
   }
   
   
        const giftWrapFee = 1.0;
        return giftWrapFee * totalQuantity;
    }
   
   
    calculateDiscount() {
      this.inputDiscount();
      let maxi = 0.0;
      Object.entries(this.discounts).forEach(([key, value])=>{
        maxi = Math.max(maxi , value);
      })
      return maxi;
    }
   
   
    calculateTotal() {
        const subtotal = this.calculateSubtotal();
        const discount = this.calculateDiscount();
        const shippingFee = this.calculateShippingFee();
        const giftWrapFee = this.calculateGiftWrapFee();
        return subtotal - discount + shippingFee + giftWrapFee;
    }
   
   
    displayReceipt() {
        console.log("js code############")
        console.log("\nReceipt:");
   
   
        for (const [productName, product] of Object.entries(this.products)) {
            const quantity = this.quantities[productName];
            const totalAmount = product.getPrice() * quantity;
            console.log(`${productName} - Quantity: ${quantity}, Total: $${totalAmount.toFixed(2)}`);
        }
   
   
        const subtotal = this.calculateSubtotal();
        console.log(`\nSubtotal: $${subtotal.toFixed(2)}`);
   
   
        const discount = this.calculateDiscount();
        if (discount > 0.0) {
            console.log(`Discount Applied: -$${discount.toFixed(2)}`);
        }
   
   
        const shippingFee = this.calculateShippingFee();
        console.log(`Shipping Fee: $${shippingFee.toFixed(2)}`);
   
   
        const giftWrapFee = this.calculateGiftWrapFee();
        console.log(`Gift Wrap Fee: $${giftWrapFee.toFixed(2)}`);
   
   
        const total = this.calculateTotal();
        console.log(`\nTotal: $${total.toFixed(2)}`);
    }
   }
   
   
   const products = {
    "Product A": new Product("Product A", 20),
    "Product B": new Product("Product B", 40),
    "Product C": new Product("Product C", 50),
   };
   
   
   const cart = new ShoppingCart(products);
   
   
   cart.inputQuantities();
   cart.inputGiftWrapping();
   cart.displayReceipt();
   
   
   
   
   