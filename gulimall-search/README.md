
# 创建索引
> PUT product

> "type": "nested" 嵌入式类型，相当于List<Object>

```json
{
  "mappings": {
    "properties": {
      "skuId": {
        "type": "long"
      },
      "spuId": {
        "type": "keyword"
      },
      "skuTitle": {
        "type": "text",
        "analyzer": "ik_smart"
      },
      "skuPrice": {
        "type": "keyword"
      },
      "skuImg": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "saleCount": {
        "type": "long"
      },
      "hasStock": {
        "type": "boolean"
      },
      "hotScore": {
        "type": "long"
      },
      "brandId": {
        "type": "long"
      },
      "catalogId": {
        "type": "long"
      },
      "brandName": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "brandImg": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "catalogName": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "attrs": {
        "type": "nested",
        "properties": {
          "attrId": {
            "type": "long"
          },
          "attrName": {
            "type": "keyword",
            "index": false,
            "doc_values": false
          },
          "attrValue": {
            "type": "keyword"
          }
        }
      }
    }
  }
}
```

## add data to up

```json
[
  {
    "skuId": 9,
    "spuId": 13,
    "skuTitle": " Apple iPhone 11 (A2223)  黑色 128GB 移动联通电信4G手机 双卡双待 最后几件优惠",
    "skuPrice": 5999.0000,
    "skuImg": "https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-27/bc4825d6-2a6c-43f8-8d75-5f35b77b9514_a2c208410ae84d1f.jpg",
    "saleCount": 1000,
    "hasStock": true,
    "hotScore": 800,
    "brandId": 12,
    "catalogId": 225,
    "brandName": "Apple",
    "brandImg": "https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-18/819bb0b1-3ed8-4072-8304-78811a289781_apple.png",
    "catalogName": "手机",
    "attrs": [
      {
        "attrName": "颜色",
        "attrValue": "黑色"
      },
      {
        "attrName": "内存",
        "attrValue": "8GB"
      }
    ]
  }
]
```