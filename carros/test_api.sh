#!/bin/bash

echo "🚀 Testing Car Rental API on port 8081"
echo "========================================="

echo ""
echo "1️⃣ Testing CREATE Contratante (should return ID):"
echo "---------------------------------------------------"
curl -X POST "http://localhost:8081/contratante/create" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678901",
    "profissao": "Engenheiro",
    "entidade": "Tech Corp"
  }' | jq .

echo ""
echo ""
echo "2️⃣ Testing GET All Contratantes:"
echo "------------------------------"
curl -X GET "http://localhost:8081/contratante" | jq .

echo ""
echo ""
echo "3️⃣ Testing GET Contratante by ID (ID=1):"
echo "---------------------------------------"
curl -X GET "http://localhost:8081/contratante/1" | jq .

echo ""
echo "✅ API Test Complete!"