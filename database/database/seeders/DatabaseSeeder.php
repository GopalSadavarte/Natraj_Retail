<?php

namespace Database\Seeders;

use Database\Factories\UserFactory;
// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use App\Models\Product;
use Database\Factories\CustomerFactory;
use Database\Factories\GroupFactory;
use Database\Factories\InventoryFactory;
use Database\Factories\SalesReturnFactory;
use Database\Factories\SubGroupFactory;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        UserFactory::new()->create();
        GroupFactory::new()->create();
        SubGroupFactory::new()->create();
        for ($i = 1; $i <= 100000; $i++) {
            Product::create([
                'p_id' => $i,
                'barcode_no' => '00' . $i,
                'product_name' => strtoupper(fake()->unique()->name()),
                'group_id' => 1,
                'sub_group_id' => 1,
                'sale_rate' => fake()->numberBetween(30, 500),
                'product_mrp' => fake()->numberBetween(500, 1000),
                'discount' => fake()->numberBetween(0, 30),
                'discount_on' => 'sale rate',
                'wholesale_rate' => 30,
                'wholesale_quantity' => 50,
                'user_id' => 1
            ]);
        }

        $i = 0;
        while ($i <= 1) {
            for ($j = 1; $j <= 100000; $j++) {
                InventoryFactory::new()->create();
            }
            $i++;
        }

        for ($i = 0; $i < 10; $i++) {
            CustomerFactory::new()->create();
            SalesReturnFactory::new()->create();
        }
    }
}
