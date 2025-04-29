# Vector Databases

## 📚 PostGre DB
- After installing PostGre Db, We need to install PGVector extension
  Please refer this link to install https://github.com/pgvector/pgvector

## 📑 Input 
### After spinning up the DB, Use this api to insert data into DB
POST Rest Api: http://localhost:8080/api/chat/userPrompt/rag/addData POST
Data Sample used: [input sample](resources/input.json)

### Test your Api Using below Request

curl --location 'http://localhost:8080/api/chat/userPrompt/rag/response' \
--header 'Content-Type: application/json' \
--data '{
"input": "Movies released before 2010"
}'

