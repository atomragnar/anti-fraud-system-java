# Anti-Fraud-System

This is the *Anti fraud systemr* project I made myself.


<p>
Learning outcomes
Create a RESTfull web service using SpringBoot, learn the basics of user authentication and authorization. Get to know the fundamentals of fraud detection and rule-based systems.
</p>

<p>

This project demonstrates (in a simplified form) the principles of anti-fraud systems in the financial sector. 
For this project, we will work on a system with an expanded role model, a set of REST endpoints responsible for interacting with users, and an internal transaction validation logic based on a set of heuristic rules.
</p>


<table border="1" cellpadding="1" cellspacing="1" style="width: 700px;">
<tbody>
<tr>
<th>
<table>
<tbody>
<tr>
<th>Transaction Feedback →</th>
</tr>
<tr>
<th style="text-align: left;">Transaction Validity ↓</th>
</tr>
</tbody>
</table>
</th>
<th>ALLOWED</th>
<th>MANUAL_PROCESSING</th>
<th>PROHIBITED</th>
</tr>
<tr>
<th>ALLOWED</th>
<td>Exception</td>
<td>↓ max ALLOWED</td>
<td>
<p>↓ max ALLOWED</p>
<p>↓ max MANUAL</p>
</td>
</tr>
<tr>
<th>MANUAL_PROCESSING</th>
<td>↑ max ALLOWED</td>
<td>Exception</td>
<td>↓ max MANUAL</td>
</tr>
<tr>
<th>PROHIBITED</th>
<td>
<p>↑ max ALLOWED</p>
<p>↑ max MANUAL</p>
</td>
<td>↑ max MANUAL</td>
<td>Exception</td>
</tr>
</tbody>
</table>

<br/><br/>



Here's the link to the project: https://hyperskill.org/projects/232

Check out my profile: https://hyperskill.org/profile/344529437
