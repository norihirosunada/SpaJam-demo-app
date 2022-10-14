#!/usr/bin/zsh

# Face APIの動作確認
source ./.env

# Kishida Face
curl $AzureEndPoint/face/v1.0/detect?returnFaceAttributes=age%2Cgender%2CheadPose%2Csmile%2CfacialHair%2Cglasses%2Cemotion%2Chair%2Cmakeup%2Cocclusion%2Caccessories%2Cblur%2Cexposure%2Cnoise \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://www.kantei.go.jp/jp/rekidainaikaku/img/rekidai-index-100-kishida.jpg"}' | jq .

# Kishida Naikaku
curl $AzureEndPoint/face/v1.0/detect \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://www.nippon.com/ja/ncommon/contents/japan-data/283192/283192.jpg"}' | jq .

# Create FaceList
faceListId=sample_face_list
curl $AzureEndPoint/face/v1.0/facelists/${faceListId} \
-X PUT \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{name: "sample_list", userData: "userData sample"}' | jq .

# Add Face to FaceList
curl $AzureEndPoint/face/v1.0/facelists/${faceListId}/persistedfaces \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{url: "https://www.kantei.go.jp/jp/rekidainaikaku/img/rekidai-index-100-kishida.jpg"}' | jq .

# Find Similar FaceList
curl $AzureEndPoint/face/v1.0/findsimilars \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{faceId: "1aac37b8-e71c-4bc1-ab15-5a49b126c527", faceIds: ["67dc961b-8491-4d1a-987a-af13e27891d8", "9ba463e6-eeae-44e9-bcb2-521cf749f128"]}' | jq .

# Delete FaceId from FaceList
curl $AzureEndPoint/face/v1.0/facelists/${faceListId}/persistedfaces/9f323ca3-3970-4108-9e8f-e90ad4fc889f \
-X DELETE \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" | jq .

# Get FaceIds from FaceList
curl $AzureEndPoint/face/v1.0/facelists/${faceListId} \
-X GET \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" | jq .

# Get FaceLists
curl $AzureEndPoint/face/v1.0/facelists \
-X GET \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" | jq .

# Edit FaceIds from FaceList
curl $AzureEndPoint/face/v1.0/facelists/${faceListId} \
-X PATCH \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d '{name: "sample_name", userData: "sample_user_data"}' | jq .
