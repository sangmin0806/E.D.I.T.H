from flask import Blueprint, request, jsonify
from app.services import reviewer
from app.services import portfolio

# Blueprint 생성
routes_bp = Blueprint('routes', __name__)

@routes_bp.route('/rag/health-check', methods=['GET'])
def health_check():
    return "I'm Alive!!!"

@routes_bp.route('/rag/portfolio', methods=['POST'])
def portfolio_make():
    data = request.get_json()
    user_id = data['userId']
    summaries = data.get('summaries')
    merge_request = data.get('mergeRequest')

    result = portfolio.make_portfolio(user_id, summaries, merge_request)
    if result:
        return jsonify({'status': 'success', 'portfolio': result}), 200
    return jsonify({'status': 'fail'}), 400


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
