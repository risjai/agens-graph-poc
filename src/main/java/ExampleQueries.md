### Return list of products viewed and purchased by individualId :12
MATCH (a:individual {id: '12'})-[:transactions]->(o:orders)-[:purchased]->(purchasedProduct:product)
WHERE EXISTS ((a)-[:viewed]->(purchasedProduct))
RETURN DISTINCT purchasedProduct.name AS productName

```
 productname
-------------
 "Product6"
(1 row)
```

### Return list of products purchased by individualId :12 but not viewed
MATCH (a:individual {id: '12'})-[:transactions]->(o:orders)-[:purchased]->(purchasedProduct:product)
WHERE NOT EXISTS ((a)-[:viewed]->(purchasedProduct))
RETURN DISTINCT purchasedProduct.name AS productName

```
 productname
-------------
 "Product3"
(1 row)
```


### List all the products where viewedCount > 1
MATCH (p:product )<-[:viewed]-(individual:individual)
WITH p, COUNT(individual) AS viewedCount
WHERE viewedCount > 1
RETURN p.name AS productName, viewedCount
```
productname | viewedcount
-------------+-------------
"Product1"  | 2
"Product6"  | 2
(2 rows)
```

### List all individuals who purchased p6
MATCH (i:individual)-[:transactions]->(o:orders)-[:purchased]->(p:product {id: 'p6'})
RETURN i.name AS individualName
```
individualname
----------------
"Sonal"
"Rishabh"
(2 rows)
```

### Find customers where successratio is more than 20%
MATCH (c:individual)-[:viewed]->(p:product)
WITH c, COUNT(p) AS viewedCount
MATCH (c)-[:transactions]->(:orders)-[:purchased]->(purchasedProduct:product)
WITH c, viewedCount, COUNT(purchasedProduct) AS purchasedCount
WITH c, viewedCount, purchasedCount, (purchasedCount * 1.0 / viewedCount) AS successRatio
WHERE successRatio > 0.2
RETURN c.name AS customerName, viewedCount, purchasedCount, successRatio
```
customername | viewedcount | purchasedcount |      successratio
--------------+-------------+----------------+------------------------
"Rishabh"    | 2           | 2              | 1.00000000000000000000
"Sonal"      | 1           | 1              | 1.00000000000000000000
(2 rows)
```
