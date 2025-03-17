from flask import Flask, request, jsonify
from flask_cors import CORS
from sklearn.metrics.pairwise import cosine_similarity
from sentence_transformers import SentenceTransformer
import numpy as np
import random

app = Flask(__name__)

CORS(app, resources={r"/analyze-achievement": {"origins": "*"}})

# Load pre-trained model
model = SentenceTransformer("multi-qa-mpnet-base-dot-v1")

# Knowledge base
hireexpert_bot = {
    "How do I set up my profile?": {
        "steps": "You can set up your profile by navigating to the Profile Page. There, you can update your personal information, your role, and company details.",
        "redirect": "/dashboard/profile",
    },

    "How to add a recruit?": {
        "steps": "You can create a new recruit by navigating to the Hiring Board page. Click the right-most button to add a recruit, fill the form, and submit.",
        "redirect": "/dashboard/hiringboard",
    },

    "How do I upload resumes?": {
        "steps": "To upload resumes, follow these steps:\n1. Go to the Resume Page.\n2. Select a specific recruit.\n3. Click on Upload Resumes and choose the files.",
        "redirect": "/dashboard/upload",
    },

    "How do you analyze resumes?": {
        "steps": "We use AI parsing to extract key data like:\n- Skills & Experience\n- Education\n- Job Fit Score\nWe rank resumes based on:\n1. Positive Scores: Match with job description and experience.\n2. Negative Scores: Penalty for false information.",
        "redirect": "Redirecting you to the Resume Analysis Page...",
    },

    "How do I manage recruits?": {
        "steps": "In HireExpert, you can:\n1. Create a new recruit.\n2. View recruit details and performance graphs.\n3. Edit or Delete existing recruits.",
        "redirect": "#",
    },

    "Can I see hiring statistics?": {
        "steps": "On the Home Page, you can view:\n📊 Total Recruits: How many hiring processes you’ve created.\n📊 Resume Count: Number of resumes uploaded.\n📊 Shortlisted & Rejected: Candidate outcomes.",
        "redirect": "/dashboard",
    },

    "How do I send interview emails?": {
        "steps": "Once you've shortlisted candidates:\n1. Go to the Upload Page.\n2. Select the candidates.\n3. Click Send Email to send customized interview invitations.",
        "redirect": "/dashboard/upload",
    },

    "Can I sort recruits in the Hiring Board?": {
        "steps": "Yes, you can sort recruits in the Hiring Board by the status. This will help you find the correct recruit.",
    },

    "Can I search and filter resumes on the Upload Page?": {
        "steps": "Yes! On the Upload Page, you can search and filter resumes based on specific skills, experience, and other criteria to narrow down the best candidates.",
    },

    "Can I view resumes in a readable template?": {
        "steps": "Yes, you can view the resumes in our readable templates for easier review.",
    },

    "Tell about yourself": {
        "steps": "I'm HireExpert, your smart hiring assistant! I help you manage recruits, analyze resumes, send interview emails, and view hiring statistics. Let me know what you'd like to do next!",
    },

    "fallback": [
        "I didn't understand that. Can you rephrase it?",
        "I'm here to assist with profile setup, resume handling, recruit management, statistics, and emails. What would you like to do?"
    ]
}

# Cache for pending suggestions and actions
pending_suggestion = {}
pending_redirect = {}

# Precompute embeddings for questions
questions = list(hireexpert_bot.keys())
question_embeddings = model.encode(questions)

def compare_questions(user_id, user_question):
    global pending_suggestion, pending_redirect

    # Standardize "yes" responses
    yes_responses = ["yes", "y", "sure", "yeah"]

    # ✅ Confirm pending actions only if relevant
    if user_question.lower() in yes_responses:
        if user_id in pending_redirect:
            confirmed_redirect = pending_redirect.pop(user_id)
            return confirmed_redirect

        if user_id in pending_suggestion:
            confirmed_question = pending_suggestion.pop(user_id)
            answer = hireexpert_bot[confirmed_question]

            # Handle redirect if present
            if "redirect" in answer:
                pending_redirect[user_id] = answer["redirect"]
                return f"{answer['steps']}\nWould you like to be redirected? (Reply 'Yes' to confirm)"

            return answer["steps"]

        # No pending context
        return "I'm not sure what you're confirming. Could you clarify?"

    # 🟢 Clear old pending actions when a new question is asked
    pending_suggestion.pop(user_id, None)
    pending_redirect.pop(user_id, None)

    # Compare user question with precomputed embeddings
    user_embedding = model.encode([user_question])
    similarity_scores = cosine_similarity(user_embedding, question_embeddings)[0]

    # Find best match
    best_match_index = np.argmax(similarity_scores)
    best_match_score = similarity_scores[best_match_index]
    best_match_question = questions[best_match_index]

    print(f"[User ID: {user_id}] User: {user_question}")
    print(f"Matched: {best_match_question} (Score: {best_match_score:.2f})")

    # ✅ High-confidence match (Above 0.7)
    if best_match_score > 0.7:
        answer = hireexpert_bot[best_match_question]

        # Handle redirect if present
        if "redirect" in answer:
            pending_redirect[user_id] = answer["redirect"]
            return f"{answer['steps']}\nWould you like to be redirected? (Reply 'Yes' to confirm)"

        return answer["steps"]

    # ✅ Medium-confidence suggestion (0.4 - 0.7)
    elif best_match_score > 0.4:
        pending_suggestion[user_id] = best_match_question
        return f"Did you mean: '{best_match_question}'? (Reply 'Yes' to confirm)"

    # ✅ Low-confidence fallback (Below 0.4)
    return random.choice(hireexpert_bot["fallback"])

@app.route("/analyze-achievement", methods=["POST"])
def giveResponseToHr():
    global pending_suggestion, pending_redirect

    data = request.json
    question = data.get("question")
    user_id = data.get("user_id")

    if not question or not user_id:
        return jsonify({"error": "Missing 'question' or 'user_id' in request"}), 400

    # Process question and generate response
    response = compare_questions(user_id, question)

    return jsonify({"response": response})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5006, debug=True)
