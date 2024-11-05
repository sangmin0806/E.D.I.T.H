from flask import Blueprint, request, jsonify
from app.services import reviewer
from app.services import portfolio

# Blueprint 생성
routes_bp = Blueprint('routes', __name__)

@routes_bp.route('/rag/health-check', methods=['GET'])
def health_check():
    return "I'm Alive!!!"

@routes_bp.route('/rag/portfolio', methods=['GET'])
def portfolio():
    data = request.get_json()
    url = data.get('url')
    token = data.get('token')
    projectId = data.get('projectId')
    branch = data.get('branch')
    portfolios = data.get('portfolios')


@routes_bp.route('/rag/code-review', methods=['POST'])
def code_review():
    data = request.get_json()
    url = data.get('url')
    token = data.get('token')
    projectId = data.get('projectId')
    branch = data.get('branch')
    commits = data.get('commits')

    review, portfolio = reviewer.getCodeReview(url, token, projectId, branch, commits)
    if review and portfolio:
        return jsonify({'status': 'success', 'review': review, 'summary': portfolio})
    else:
        return jsonify({'status': 'fail', 'message': '코드 리뷰 생성 중 오류가 발생했습니다.'}), 500
