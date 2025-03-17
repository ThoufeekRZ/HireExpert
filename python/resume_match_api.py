from flask import Flask, request, jsonify
from sklearn.metrics.pairwise import cosine_similarity
from sentence_transformers import SentenceTransformer


app = Flask(__name__)
# Load an ai model
model = SentenceTransformer("multi-qa-mpnet-base-dot-v1")

def get_similarity(text1,text2):
    embeddings = model.encode([text1,text2])
    similarity_score = cosine_similarity([embeddings[0]], [embeddings[1]])[0][0]
    return float(similarity_score)


@app.route("/match",methods=["POST"])
def match_resume_descriptions():
    data = request.json

    job_description = data.get("jobDescription","")
    candidate_description = data.get("candidateDescription",[])
    score =0

    jobTitle = data.get("jobTitle","")
    candidateTitle = data.get("candidateTitle","")


    if not job_description or not candidate_description or not jobTitle or not candidateTitle: 
        return jsonify({"error": "Missing data"}), 400
    
    title_score = get_similarity(jobTitle, candidateTitle) * 20
    responsibility_score = (sum(get_similarity(job_description, desc) for desc in candidate_description) / len(candidate_description)) * 20

    score = title_score + responsibility_score

    return jsonify({"score": score}), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5005, debug=True)
    
  