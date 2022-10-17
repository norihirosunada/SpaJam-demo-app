#!/usr/bin/zsh

# Languageの動作確認
source ./.env

# 言語検出
# curl "$AzureEndPoint/language/:analyze-text?api-version=2022-05-01" \
# -X POST \
# -H "Content-Type: application/json" \
# -H "Ocp-Apim-Subscription-Key:$AzureAPIKey" \
# -d \
# '
# {
#     "kind": "LanguageDetection",
#     "parameters": {
#         "modelVersion": "latest"
#     },
#     "analysisInput":{
#         "documents":[
#             {
#                 "id":"1",
#                 "text": "This is a document written in English."
#             }
#         ]
#     }
# }
# ' | jq .
#
# # 感情分析とオピニオンマイニング
# curl "$AzureEndPoint/language/:analyze-text?api-version=2022-05-01" \
# -X POST \
# -H "Content-Type: application/json" \
# -H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
# -d \
# '
# {
#     "kind": "SentimentAnalysis",
#     "parameters": {
#         "modelVersion": "latest",
#         "opinionMining": "True"
#     },
#     "analysisInput":{
#         "documents":[
#             {
#                 "id":"1",
#                 "language":"ja",
#                 "text": "私も小さい頃好きだった絵本。もちろん子供たちも大好きな絵本。みんなで美味しそうに木に腰掛けてカステラ食べるシーンではどんなどうぶつがいるか？とか何匹いるか？とか違った風にも楽しめます。読み終わったあとはもちろんカステラが食べたい～ってなりますね。"
#             }
#         ]
#     }
# }
# ' | jq .

# 個人情報の抽出
# curl "$AzureEndPoint/language/:analyze-text?api-version=2022-05-01" \
# -X POST \
# -H "Content-Type: application/json" \
# -H "Ocp-Apim-Subscription-Key:$AzureAPIKey" \
# -d \
# '
# {
#     "kind": "PiiEntityRecognition",
#     "parameters": {
#         "modelVersion": "latest"
#     },
#     "analysisInput":{
#         "documents":[
#             {
#                 "id":"1",
#                 "language": "en",
#                 "text": "Call our office at 312-555-1234, or send an email to support@contoso.com"
#             }
#         ]
#     }
# }
# ' | jq .

# 固有表現（固有名詞や日付、数量)の抽出
curl "$AzureEndPoint/language/:analyze-text?api-version=2022-05-01" \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key:$AzureAPIKey" \
-d \
'
{
    "kind": "EntityRecognition",
    "parameters": {
        "modelVersion": "latest"
    },
    "analysisInput":{
        "documents":[
            {
                "id":"1",
                "language": "en",
                "text": "I had a wonderful trip to Seattle last week."
            }
        ]
    }
}
' | jq .

# キーフレーズ（文章中の特徴的なフレーズ）の抽出
curl "$AzureEndPoint/language/:analyze-text?api-version=2022-05-01" \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d \
'
{
    "kind": "KeyPhraseExtraction",
    "parameters": {
        "modelVersion": "latest"
    },
    "analysisInput":{
        "documents":[
            {
                "id":"1",
                "language":"en",
                "text": "Dr. Smith has a very modern medical office, and she has great staff."
            }
        ]
    }
}
' | jq .

# エンティティ リンクの設定
# キーワードとWikiのリンクを取得
curl "$AzureEndPoint/language/:analyze-text?api-version=2022-05-01" \
-X POST \
-H "Content-Type: application/json" \
-H "Ocp-Apim-Subscription-Key: $AzureAPIKey" \
-d \
'
{
    "kind": "EntityLinking",
    "parameters": {
        "modelVersion": "latest"
    },
    "analysisInput":{
        "documents":[
            {
                "id":"1",
                "language":"en",
                "text": "Microsoft was founded by Bill Gates and Paul Allen on April 4, 1975."
            }
        ]
    }
}
' | jq .
