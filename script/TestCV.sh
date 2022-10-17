#!/usr/bin/zsh

# ComputerVisionの動作確認
source ./.env

# 画像の解析
curl "$AzureEndPoint/vision/v3.2/analyze?visualFeatures=Categories,Description&details=Landmarks" \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "http://upload.wikimedia.org/wikipedia/commons/3/3c/Shaki_waterfall.jpg"}' | jq .


# OCR
curl $AzureEndPoint/vision/v3.2/ocr?language=ja \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://www.kinokuniya.co.jp/images/goods/ar2/web/imgdata2/large/41010/4101010013.jpg"}' | jq .

# OCR 動作しない
# curl "https://spajam-cvtest.cognitiveservices.azure.com/vision/v3.2/read/analyze" \
# -X POST \
# -H "Content-Type: application/json" \
# -H "Ocp-Apim-Subscription-Key: 214729a67b5a408285e28d93ca3cd352" \
# -d '{url: "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Atomist_quote_from_Democritus.png/338px-Atomist_quote_from_Democritus.png"}' | jq .

# 画像の説明
curl $AzureEndPoint/vision/v3.2/describe \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://www.kinokuniya.co.jp/images/goods/ar2/web/imgdata2/large/41010/4101010013.jpg"}' | jq .

# 物体検出
curl $AzureEndPoint/vision/v3.2/detect \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://d1sw4fcdq5we39.cloudfront.net/wp-content/uploads/2021/04/26120921/instasize_2010021703361.jpg"}' | jq .

# サムネイル生成
curl "$AzureEndPoint/vision/v3.2/generateThumbnail?width=300&height=300" \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://d1sw4fcdq5we39.cloudfront.net/wp-content/uploads/2021/04/26120921/instasize_2010021703361.jpg"}' --output out.bin

# 関心領域の取得
curl "$AzureEndPoint/vision/v3.2/areaOfInterest" \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://d1sw4fcdq5we39.cloudfront.net/wp-content/uploads/2021/04/26120921/instasize_2010021703361.jpg"}'| jq .

# モデル一覧
curl "$AzureEndPoint/vision/v3.2/models" \
-X GET \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" | jq .

# タグ（画像のコンテンツ）検出
curl "$AzureEndPoint/vision/v3.2/tag" \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://d1sw4fcdq5we39.cloudfront.net/wp-content/uploads/2021/04/26120921/instasize_2010021703361.jpg"}'| jq .
